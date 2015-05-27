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
    protected byte[] encrypt(byte[] block, byte[] previousEncryptedBlock) {
        byte[] newArray = new byte[block.length];
        for (int i = 0; i < newArray.length; i++)
            newArray[i] = (byte) ((block[i] & 0xFF) ^ (previousEncryptedBlock[i] & 0xFF));

        return newArray;
    }

    @Override
    protected byte[] decrypt(byte[] block, byte[] previousEncryptedBlock) {
        return encrypt(block, previousEncryptedBlock);
    }
}
