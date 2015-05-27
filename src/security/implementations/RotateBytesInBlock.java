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

        if (shiftByAmount < 0 || shiftByAmount >= blockSize)
            throw new IllegalArgumentException("shiftByAmount must be between 0 and blockSize");

        this.shiftByAmount = shiftByAmount;
    }

    @Override
    protected byte[] encrypt(byte[] block, byte[] previousEncryptedBlock) {
        return rotate(block, shiftByAmount);
    }

    @Override
    protected byte[] decrypt(byte[] block, byte[] previousEncryptedBlock) {
        int amount = block.length - shiftByAmount;
        while (amount < 0)
            amount += block.length;

        return rotate(block, amount);
    }

    static byte[] rotate(byte[] block, int howMuch) {
        howMuch = howMuch % block.length;

        byte[] encryptedBlock = new byte[block.length];
        int placeToCopyFromStart = encryptedBlock.length - howMuch;

        System.arraycopy(block, howMuch, encryptedBlock, 0, placeToCopyFromStart);
        System.arraycopy(block, 0, encryptedBlock, placeToCopyFromStart, howMuch);

        return encryptedBlock;
    }

    @Override
    public String toString() {
        return "RotateBytesInBlock(shift: " + shiftByAmount + ", blockSize: " + blockSize + ")";
    }
}
