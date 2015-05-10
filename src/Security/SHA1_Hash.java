package Security;

import Logging.LogType;
import Logging.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by Jableader on 10/5/2015.
 * Mostly following the Psuedocode SHA-1 Implementation
 */
public class SHA1_Hash extends MessageDigest {
    final Logger logger;

    public SHA1_Hash(Logger logger) {
        super("ME-SHA1");

        this.logger = logger;
    }


    @Override
    protected void engineUpdate(byte input) {

    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {

    }

    @Override
    protected byte[] engineDigest() {
        return new byte[0];
    }

    @Override
    protected void engineReset() {

    }
}
