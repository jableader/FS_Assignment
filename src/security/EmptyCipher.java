package security;

/**
 * Created by Jableader on 13/5/2015.
 */
public class EmptyCipher extends Cipher {
    public byte encrypt(byte b, int pos) {
        return b;
    }

    public byte decrypt(byte b, int pos) {
        return b;
    }
}