package Server.management;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Login {
    public final byte[] password;
    public final String id;

    public Login(String username, byte[] password){
        this.id = username;
        this.password = password;
    }
}