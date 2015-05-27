package security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * Buffers its input into blocks of bytes, then encrypts them in blocks. If the input bytes do not evenly fit into the
 * blocks then the last byte[] will be shorter to make up for the difference.
 *
 * Fills up a buffer and encrypts/decrypts in blocks. It attempts to reuse byte arrays
 * where possible to avoid a bucket load of objects being dumped onto the heap, although practically speaking it will
 * anyway
 */
public abstract class BlockCipher implements Cipher {
    protected final byte[] initialisationVector;
    protected final int blockSize;

    protected BlockCipher(int blockSize) {
        this(new byte[blockSize]);
    }

    protected BlockCipher(byte[] initialisationVector) {
        this.initialisationVector = initialisationVector.clone();
        this.blockSize = initialisationVector.length;
    }

    public OutputStream getCipheringStream(OutputStream baseStream) {
        return new BlockOutputStream(baseStream);
    }

    public InputStream getDecipheringStream(InputStream baseStream) {
        return new BlockInputStream(baseStream);
    }

    protected abstract byte[] encrypt(byte[] block, byte[] previousEncryptedBlock);
    protected abstract byte[] decrypt(byte[] block, byte[] previousEncryptedBlock);

    private class BlockOutputStream extends OutputStream {
        final OutputStream baseStream;

        int bufferPosition = 0;
        byte[] buffer = new byte[initialisationVector.length];
        byte[] encryptedPreviousBuffer = initialisationVector.clone();

        private BlockOutputStream(OutputStream baseStream) {
            this.baseStream = baseStream;
        }

        @Override
        public void write(int b) throws IOException {
            buffer[bufferPosition] = (byte)b;

            if (++bufferPosition == buffer.length) {
                encryptedPreviousBuffer = encrypt(buffer, encryptedPreviousBuffer);
                baseStream.write(encryptedPreviousBuffer);

                bufferPosition = 0;
            }
        }

        @Override
        public void flush() throws IOException {
            if (bufferPosition > 0) {
                baseStream.write(encrypt(Arrays.copyOf(buffer, bufferPosition), encryptedPreviousBuffer));
                bufferPosition = 0;
            }

            baseStream.flush();
        }

        @Override
        public void close() throws IOException {
            flush();

            super.close();
            baseStream.close();
        }
    }

    private class BlockInputStream extends InputStream {
        final InputStream baseStream;

        byte[] decryptedCurrentBlock = new byte[initialisationVector.length];
        byte[] encryptedPreviousBlock = initialisationVector.clone();

        int bufferPosition = Integer.MAX_VALUE; //Ensure that buffer loaded on first call

        private BlockInputStream(InputStream baseStream) {
            this.baseStream = baseStream;
        }

        @Override
        public int read() throws IOException {
            if (bufferPosition >= decryptedCurrentBlock.length) {
                byte[] encryptedNextBlock = decryptedCurrentBlock;
                int amountRead = baseStream.read(encryptedNextBlock);

                if (amountRead == -1)
                    return -1;

                else if (amountRead < encryptedNextBlock.length)
                    encryptedNextBlock = Arrays.copyOf(encryptedNextBlock, amountRead);

                decryptedCurrentBlock = decrypt(encryptedNextBlock, encryptedPreviousBlock);
                encryptedPreviousBlock = encryptedNextBlock;

                bufferPosition = 0;
            }

            return decryptedCurrentBlock[bufferPosition++];
        }

        @Override
        public void close() throws IOException {
            super.close();
            baseStream.close();
        }
    }
}
