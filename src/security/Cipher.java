package security;

import sun.misc.IOUtils;

import java.io.*;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * A very basic interface to create a cipher to wrap a stream
 */
public interface Cipher {
    OutputStream getCipheringStream(OutputStream s);
    InputStream getDecipheringStream(InputStream s);

    default byte[] encryptBytes(byte[] b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(b.length);
        OutputStream cipherStream = getCipheringStream(bos);

        try {
            cipherStream.write(b);
            cipherStream.flush();
        } catch (IOException ex) {

            ex.printStackTrace();
            return null;
        }

        return bos.toByteArray();
    }

    default byte[] decryptBytes(byte[] b) {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        InputStream decipheredInputStream = getDecipheringStream(bis);

        try {
            return IOUtils.readFully(decipheredInputStream, -1, true);
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }
}
