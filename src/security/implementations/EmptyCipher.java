package security.implementations;

import security.StreamCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class EmptyCipher extends StreamCipher {
    public byte encrypt(byte b, int pos) {
        return b;
    }

    public byte decrypt(byte b, int pos) {
        return b;
    }
}