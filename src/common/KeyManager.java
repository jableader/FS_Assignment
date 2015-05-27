package common;

import logging.LogType;
import logging.Logger;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static common.Tools.toBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class KeyManager {
    final SecureRandom keyGenerator = new SecureRandom();
    final Map<Login, Key> keys = new HashMap<>();
    final Logger logger;

    public KeyManager(Logger logger) {
        this.logger = logger;
    }

    public void registerKey(Login login, Key key) {
        keys.put(login, key);
    }

    public Key generateKey(Date expiry) {
        byte[] bytes = new byte[128];
        keyGenerator.nextBytes(bytes);

        Key key = new Key(expiry, bytes);

        logger.Log(LogType.Verbose, "Generated key " + toBase64(bytes));
        return key;
    }
}
