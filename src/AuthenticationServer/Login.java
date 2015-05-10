package AuthenticationServer;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Login {
    public final String password;
    public final String username;

    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }

    public byte[] passwordAsBytes() {
        try {
            return username.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex){
            throw new UnsupportedOperationException("Cant do without ASCII", ex);
        }
    }
}