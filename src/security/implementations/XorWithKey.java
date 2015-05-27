package security.implementations;

import security.StreamCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class XorWithKey extends StreamCipher {
    private final byte[] key;

    public XorWithKey(byte[] key) {
        this.key = key;
    }

    @Override
    protected byte encrypt(byte input, int position) {
        int encrypted = input ^ key[position % key.length];
        return (byte) (encrypted & 0xFF);
    }

    @Override
    protected byte decrypt(byte input, int position) {
        return encrypt(input, position); //Identical each way
    }
}
