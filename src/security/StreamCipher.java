package security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * A simple stream cipher implementation. Encryption is passed off to sub classes
 */
public abstract class StreamCipher implements Cipher {
    protected abstract byte encrypt(byte input, int position);

    protected abstract byte decrypt(byte input, int position);

    public OutputStream getCipheringStream(OutputStream s) {
        return new EncryptedOutStream(s);
    }

    public InputStream getDecipheringStream(InputStream s) {
        return new DecryptedInStream(s);
    }

    class DecryptedInStream extends InputStream {
        final InputStream baseStream;

        public DecryptedInStream(InputStream baseStream) {
            this.baseStream = baseStream;
        }

        int position = 0;

        @Override
        public int read() throws IOException {
            int next = baseStream.read();
            return next == -1 ? -1 : decrypt((byte) (next & 0xFF), position++);
        }

        @Override
        public void close() throws IOException {
            super.close();
            baseStream.close();
        }
    }

    class EncryptedOutStream extends OutputStream {
        final OutputStream baseStream;

        public EncryptedOutStream(OutputStream innerStream) {
            this.baseStream = innerStream;
        }

        int position = 0;

        public void write(int b) throws IOException {
            baseStream.write(encrypt((byte) (b & 0xFF), position++));
        }

        @Override
        public void flush() throws IOException {
            baseStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            baseStream.close();
        }
    }
}
