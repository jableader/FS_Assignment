package security;

import logging.Logger;

/**
 * Created by Jableader on 10/5/2015.
 * Mostly following the Psuedocode SHA-1 Implementation on wikipedia
 */
public class SHA1_Hash implements Hasher {
    final Logger logger;

    public SHA1_Hash(Logger logger) {
        this.logger = logger;
    }

    @Override
    public byte[] hash(byte[] input) {
        return input;
    }
}
