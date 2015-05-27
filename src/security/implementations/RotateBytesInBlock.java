package security.implementations;

import security.BlockCipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * Rotate the bytes in a block
 */
public class RotateBytesInBlock extends BlockCipher {
    public final int shiftByAmount;

    public RotateBytesInBlock(int shiftByAmount, int blockSize) {
        super(blockSize);

        this.shiftByAmount = shiftByAmount;
    }

    @Override
    protected byte[] encrypt(byte[] block, byte[] previousBlock) {
        return rotate(block, shiftByAmount);
    }

    @Override
    protected byte[] decrypt(byte[] block, byte[] previousBlock) {
        return rotate(block, -shiftByAmount);
    }

    byte[] rotate(byte[] block, int howMuch) {
        howMuch = putWithinRange(howMuch, blockSize);

        byte[] encryptedBlock = new byte[block.length];
        System.arraycopy(block, 0, encryptedBlock, howMuch, encryptedBlock.length - howMuch);
        System.arraycopy(block, encryptedBlock.length - howMuch, encryptedBlock, 0, howMuch);

        return encryptedBlock;
    }

    private int putWithinRange(int i, int range) {
        return i < 0 ?
                (i - (-i % range)) % range :
                i % range;
    }
}
