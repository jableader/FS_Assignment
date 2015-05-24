package security;

import javax.crypto.*;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class BasicCipher extends Cipher {
    private final byte[] key;

    public BasicCipher(byte[] key) {
        this.key = key;
    }

    @Override
    protected byte encrypt(byte input, int position) {
        int encrypted = (int)(input) + (int)(key[position % key.length]);
        return (byte)(encrypted & 0xFF);
    }

    @Override
    protected byte decrypt(byte input, int position) {
        int decrypted = (int)input - (int) (key[position % key.length]);
        return (byte) (decrypted & 0xFF);
    }
}
