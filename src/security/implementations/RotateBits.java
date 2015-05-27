package security.implementations;

import security.StreamCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class RotateBits extends StreamCipher {
    final int amountToRotateBy;

    public RotateBits(int amountToRotateBy) {
        this.amountToRotateBy = amountToRotateBy;
    }

    @Override
    protected byte encrypt(byte input, int position) {
        return (byte)(((input & 0xff)  >>> amountToRotateBy) | ((input & 0xff) << (8 - amountToRotateBy)));
    }

    @Override
    protected byte decrypt(byte input, int position) {
        return (byte)(((input & 0xff) << amountToRotateBy) | ((input & 0xff) >>> (8 - amountToRotateBy)));
    }

    @Override
    public String toString() {
        return "RotateBits(3)";
    }
}
