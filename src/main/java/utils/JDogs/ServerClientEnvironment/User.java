package utils.JDogs.ServerClientChatPingPongWithThreads;


/*
this class will save the password,
the nickname and
if a user is currently logged in or not.
 */

public class User {
    private boolean isLoggedIn;
    private final String password;
    private String nickName;

    public User(String password, boolean isLoggedIn) {
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.nickName = null;

    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn () {
        isLoggedIn = true;
    }

    public void setLoggedOut () {
        isLoggedIn = false;
    }

   synchronized public void changeNickname(String newNick) {
        nickName = newNick;
    }
   synchronized public String getNickName() {
        return nickName;
    }
}
