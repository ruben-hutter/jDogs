package utils.JDogs.ServerClientChatPingPongWithThreads;

public class MessageHandler implements Runnable {
    private Queue sendToAll;
    private Queue sendToThisClient;
    private Queue receivedFromClient;
    private boolean running;
    private boolean incompleteLogin;
    private Server server;
    private ServerConnection serverConnection;
    private String userName;

    public MessageHandler(Server server,ServerConnection serverConnection, Queue sendToThisClient, Queue sendToAll, Queue receivedFromClient) {
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
    //if client is already logged in and didn`t logout

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



            } else {
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
        //after login:

        String text;
        while (running) {


            if (!receivedFromClient.isEmpty()) {
                text = receivedFromClient.dequeue();
                System.out.println(text);

                //internal commands begin with "jd "
                if(text.length() >= 4 && text.substring(0,3).equals("jd ")) {
                    messageHandling(text);
                } else {
                    sendToAll.enqueue(userName + ": " + text);
                }
            }
        }

        System.out.println(this.toString() + "  stops now");


    }


    public void messageHandling(String command) {
        System.out.println(command);

        System.out.println(command.substring(3,9));


        switch (command.substring(3,9)) {

            case "logout":
                sendToThisClient.enqueue("logout now");
                serverConnection.kill();
                running = false;
                break;

            case "whispe":
                sendToThisClient.enqueue("whisperChat is not implemented");
                break;

            default:
                sendToThisClient.enqueue("unknown command");
        }
    }

    public void kill() {
        running = false;
    }
}
