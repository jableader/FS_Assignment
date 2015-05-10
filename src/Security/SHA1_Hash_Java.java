package Security;

import Logging.LogType;
import Logging.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Thin wrapper on Javas SHA-1 to allow for logging
 * Created by Jableader on 10/5/2015.
 */
public class SHA1_Hash_Java extends MessageDigest {
    final Logger logger;
    MessageDigest delegate;

    public SHA1_Hash_Java(Logger logger) {
        super("Java_SHA1");

        this.logger = logger;
        try {
            delegate = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex){
            logger.Log(LogType.Error, "No SHA1");
        }
    }

    public void engineUpdate(byte b) {
        delegate.update(b);
    }

    public void engineUpdate(byte b[], int offset, int length) {
        delegate.update(b, offset, length);
    }

    public void engineReset() {
        delegate.reset();
    }

    public byte[] engineDigest() {
        logger.Log(LogType.Standard, "Hashing Password with Java SHA-1");

        return delegate.digest();
    }
}
