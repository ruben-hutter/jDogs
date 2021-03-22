package ServerClientExtended;


//this class will save the password and if a user is currently logged in or not
//we can add stats of the users here

public class User {
    private boolean isLoggedIn;
    private String password;

    public User(String password, boolean isLoggedIn) {
        this.password = password;
        this.isLoggedIn = isLoggedIn;

    }

    public String getPassword() {
        return password;
    }
}
