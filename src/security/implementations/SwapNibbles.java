package security.implementations;

import security.StreamCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class SwapNibbles extends StreamCipher {

    @Override
    protected byte encrypt(byte input, int position) {
        return swapNibbles(input);
    }

    @Override
    protected byte decrypt(byte input, int position) {
        return swapNibbles(input);
    }

    private byte swapNibbles(byte b) {
        int swapped = ((int)b & 0x0F) << 4 | ((int)b & 0xF0) >> 4;
        return (byte)(swapped & 0xFF);
    }
}
