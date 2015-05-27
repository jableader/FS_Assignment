package security.implementations;

import security.BlockCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class XorWithPreviousBlock extends BlockCipher {

    public XorWithPreviousBlock(byte[] initialisationVector) {
        super(initialisationVector);
    }

    @Override
    protected byte[] encrypt(byte[] block, byte[] previousBlock) {
        byte[] newArray = new byte[block.length];
        for (int i = 0; i < newArray.length; i++)
            newArray[i] = (byte) (block[i] ^ previousBlock[i]);

        return newArray;
    }

    @Override
    protected byte[] decrypt(byte[] block, byte[] previousBlock) {
        return encrypt(block, previousBlock);
    }
}
