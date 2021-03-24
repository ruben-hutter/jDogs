package JDogs.ServerClientEnvironment;

public class MessageHandlerServer implements Runnable {
    private Queue sendToAll;
    private Queue sendToThisClient;
    private Queue receivedFromClient;
    private boolean running;
    private boolean incompleteLogin;
    private Server server;
    private ServerConnection serverConnection;
    private String userName;
    private String nickName;


    public MessageHandlerServer(Server server,ServerConnection serverConnection, Queue sendToThisClient, Queue sendToAll, Queue receivedFromClient) {
        this.sendToAll = sendToAll;
        this.sendToThisClient = sendToThisClient;
        this.receivedFromClient = receivedFromClient;
        this.server = server;
        this.serverConnection = serverConnection;

        this.incompleteLogin = true;
        this.running = true;
    }

    @Override
    public void run() {
    //login
    getLoggedIn();

    //receive default nickname
    getNickname();

    String text;

    while (running) {
        if (!receivedFromClient.isEmpty()) {
                text = receivedFromClient.dequeue();
                System.out.println(text);

                //internal commands begin with "jd "
                if(text.length() >= 9 && text.substring(0,3).equals("jd ")) {
                    messageHandling(text);
                } else {

                    if (text.length() >= 3 && text.substring(0,3).equals("jd ")) {
                        sendToThisClient.enqueue("unknown command");
                    } else {

                            sendToAll.enqueue(nickName + ": " + text);

                        }
                    }
                }
            }
        System.out.println(this.toString() + "  stops now");
    }

    public void getLoggedIn() {

        while (incompleteLogin) {

            sendToThisClient.enqueue("enter UserName");
            String password;

            while (true) {

                if (!receivedFromClient.isEmpty()) {
                    this.userName = receivedFromClient.dequeue();
                    break;
                }
            }

            if (server.UserPasswordMap.containsKey(userName)) {
                //you were already logged in and did not log out
                if (server.UserPasswordMap.get(userName).isLoggedIn()) {
                    sendToThisClient.enqueue("already logged in");
                    incompleteLogin = false;
                    break;
                } else {
                    //userName is taken but person logged out
                    //ask for password
                    sendToThisClient.enqueue("userName taken...enter password");

                    while (true) {


                        if (!receivedFromClient.isEmpty()) {
                            password = receivedFromClient.dequeue();
                            if (server.UserPasswordMap.get(userName).getPassword().equals(password)) {
                                sendToThisClient.enqueue("logged in");
                                incompleteLogin = false;
                                System.out.println("login done..");

                                break;
                            }
                        }
                    }
                }
            } else {
                //userName is unknown
                sendToThisClient.enqueue("enter password phrase");
                while (true) {


                    if (!receivedFromClient.isEmpty()) {
                        password = receivedFromClient.dequeue();
                        break;
                    }
                }
                sendToThisClient.enqueue("re-enter password phrase");

                while (true) {
                    if (!receivedFromClient.isEmpty()) {
                        String password2 = receivedFromClient.dequeue();
                        if (password2.equals(password)){
                            User user = new User(password, true);
                            server.UserPasswordMap.put(userName,user);
                            sendToThisClient.enqueue("logged in");
                            System.out.println("login done..");
                            System.out.println(password2);

                            incompleteLogin = false;
                            break;
                        } else {
                            sendToThisClient.enqueue("passwords didn`t match..retry..");
                        }
                    }
                }
            }
        }
    }

    public void getNickname() {

        if (nickName == null) {
            sendToThisClient.enqueue("jd nickna");
        }


    }


    public void messageHandling(String command) {

        switch (command.substring(3,9)) {

            case "logout":
                sendToThisClient.enqueue("logout now");
                server.UserPasswordMap.get(userName).setLoggedOut();

                serverConnection.kill();
                running = false;
                break;

            case "whispe":
                sendToThisClient.enqueue("whisperChat is not implemented");

                break;
            case "nickna":
                if (command.length() < 10) {
                    sendToThisClient.enqueue("no nickname entered");
                } else {
                    nickName = command.substring(10, command.length());
                    if (server.isValidNickName(nickName)) {
                        server.UserPasswordMap.get(userName).changeNickname(nickName);
                        server.allNickNames.add(nickName);
                        sendToThisClient.enqueue("hi, user "+ userName + "! your new nickname is: " + nickName);
                    } else {
                        int number = 2;
                        while (true) {
                            if(server.isValidNickName(nickName + " " + Integer.toString(number))) {
                                nickName = nickName + " " + Integer.toString(number);
                                server.UserPasswordMap.get(userName).changeNickname(nickName);
                                server.allNickNames.add(nickName);
                                sendToThisClient.enqueue("hi, user "+ userName + "! your new nickname is: " + nickName);

                                break;
                            } else {
                                number++;
                            }
                        }

                    }
                }
                    break;
            case "users":
                sendToThisClient.enqueue("list of active users not implemented");
                break;


            default:
                sendToThisClient.enqueue("unknown command");
        }
    }

    public void kill() {
        running = false;
    }
}
