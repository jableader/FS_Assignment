package security;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * Chains together various ciphers, extremely useful to abstracting the operations of
 * each cipher
 */
public class AggregateCipher implements Cipher {
    Cipher[] ciphers;

    public AggregateCipher(Cipher... ciphers){
        this.ciphers = ciphers;
    }

    @Override
    public OutputStream getCipheringStream(OutputStream s) {
        for (Cipher c: ciphers)
            s = c.getCipheringStream(s);

        return s;
    }

    @Override
    public InputStream getDecipheringStream(InputStream s) {
        for (Cipher c: ciphers)
            s = c.getDecipheringStream(s);

        return s;
    }
}
