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
        return circularRotate(input, amountToRotateBy);
    }

    @Override
    protected byte decrypt(byte input, int position) {
        return circularRotate(input, -amountToRotateBy);
    }

    private byte circularRotate(byte b, int r) {
        return (byte)(b >> r | (b << Byte.SIZE - r));
    }
}
