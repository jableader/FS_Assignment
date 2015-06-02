package security.implementations;

import security.BlockCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class EmptyBlockCipher extends BlockCipher {

    public EmptyBlockCipher(int blockSize) {
        super(blockSize);
    }

    @Override
    protected byte[] encrypt(byte[] block, byte[] previousEncryptedBlock) {
        return block.clone();
    }

    @Override
    protected byte[] decrypt(byte[] block, byte[] previousEncryptedBlock) {
        return block.clone();
    }
}
