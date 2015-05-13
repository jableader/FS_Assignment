package AuthenticationServer;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Login {
    public final String password;
    public final String id;

    public Login(String username, String password){
        this.id = username;
        this.password = password;
    }

    public byte[] passwordAsBytes() {
        try {
            return id.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex){
            throw new UnsupportedOperationException("Cant do without ASCII", ex);
        }
    }
}