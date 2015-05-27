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
        final InputStream inStream;

        public DecryptedInStream(InputStream inStream) {
            this.inStream = inStream;
        }

        int position = 0;

        @Override
        public int read() throws IOException {
            return decrypt((byte) inStream.read(), position++);
        }
    }

    class EncryptedOutStream extends OutputStream {
        final OutputStream outStream;

        public EncryptedOutStream(OutputStream outStream) {
            this.outStream = outStream;
        }

        int position = 0;

        public void write(int b) throws IOException {
            outStream.write(encrypt((byte) b, position++));
        }
    }
}
