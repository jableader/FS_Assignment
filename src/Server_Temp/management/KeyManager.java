package Server_Temp.management;

import Logging.LogType;
import Logging.Logger;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jableader on 11/05/2015.
 */
public class KeyManager {
    final SecureRandom keyGenerator = new SecureRandom();
    final Map<Login, Key> keys = new HashMap<>();
    final Logger logger;

    public KeyManager(Logger logger) {
        this.logger = logger;
    }

    public void registerKey(Login login, Key key){
        keys.put(login, key);
    }

    public Key getRandomKey(Date expiry) {
        byte[] bytes = new byte[128];
        keyGenerator.nextBytes(bytes);

        Key key = new Key(expiry, bytes);

        logger.Log(LogType.Verbose, "Generated key", bytes);
        return key;
    }
}
