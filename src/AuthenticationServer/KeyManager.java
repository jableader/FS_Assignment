package AuthenticationServer;

import sun.rmi.runtime.Log;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jableader on 11/05/2015.
 */
public class KeyManager {
    SecureRandom keyGenerator = new SecureRandom();
    Map<Login, Key> keys = new HashMap<>();

    public void registerKey(Login login, Key key){
        keys.put(login, key);
    }

    public Key getRandomKey() {
        byte[] key = new byte[128];
        keyGenerator.nextBytes(key);

        return new Key(key);
    }
}
