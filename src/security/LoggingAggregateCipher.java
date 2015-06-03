package security;

import logging.LogType;
import logging.Logger;
import sun.misc.IOUtils;

import java.io.*;

import static common.Tools.toBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * Provides logging to the cipher methods
 */
public class LoggingAggregateCipher implements Cipher {
    private final Logger logger;
    private final Cipher[] ciphers;

    public LoggingAggregateCipher(Logger logger, Cipher... ciphers){
        this.ciphers = ciphers;
        this.logger = logger;
    }

    @Override
    public OutputStream getCipheringStream(OutputStream s) {
        return new NoisyOutputStream(s);
    }

    @Override
    public InputStream getDecipheringStream(InputStream s) {
        try {
            return new NoisyInputStream(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    private class NoisyInputStream extends InputStream {
        final InputStream baseStream;

        public NoisyInputStream(InputStream innerStream) throws IOException {
            byte[] encrypted = IOUtils.readFully(innerStream, -1, true);
            logger.Log(LogType.Cipher, "Original :: " + toBase64(encrypted));

            for (int i = ciphers.length-1; i >= 0; i--) {
                encrypted = ciphers[i].decryptBytes(encrypted);
                logger.Log(LogType.Cipher, ciphers[i] + " :: " + toBase64(encrypted));
            }

            logger.Log(LogType.Cipher, "Final as string :: " + new String(encrypted));

            baseStream = new ByteArrayInputStream(encrypted);
        }

        @Override
        public int read() throws IOException {
            return baseStream.read();
        }
    }

    private class NoisyOutputStream extends OutputStream {
        private final OutputStream baseStream;
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        public NoisyOutputStream(OutputStream baseStream) {
            this.baseStream = baseStream;
        }


        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public void flush() throws IOException {
            byte[] bs = bos.toByteArray();
            logger.Log(LogType.Cipher, "Original As String :: " + new String(bs));
            logger.Log(LogType.Cipher, "Original :: " + toBase64(bs));

            for (Cipher c : ciphers) {
                bs = c.encryptBytes(bs);
                logger.Log(LogType.Cipher, c.toString() + " :: " + toBase64(bs));
            }

            baseStream.write(bs);
            baseStream.flush();
        }

        @Override
        public void close() throws IOException {
            flush();

            super.close();
            baseStream.close();
        }
    }
}
