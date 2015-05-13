package security;

import logging.LogType;
import logging.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Thin wrapper on Javas SHA-1 to allow for logging
 *
 * This is here to allow swapping out my own implementation for a well proven one
 * for testing.
 *
 * Created by Jableader on 10/5/2015.
 */
public class SHA1_Hash_Java implements Hasher {
    final Logger logger;

    public SHA1_Hash_Java(Logger logger) {
        this.logger = logger;
    }

    @Override
    public byte[] hash(byte[] input) {
        logger.Log(LogType.Standard, "Generating hash with JAVA SHA1");

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.reset();
            return digest.digest(input);

        } catch (NoSuchAlgorithmException ex) {
            logger.Log(LogType.Error, "No SHA1 Digest");

            throw new UnsupportedOperationException("Need java's implementation of SHA1");
        }
    }
}
