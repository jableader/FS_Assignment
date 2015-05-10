package AuthenticationServer;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Login {
    public final String hashedPassword;
    public final String username;

    public Login(String username, String hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
    }
}