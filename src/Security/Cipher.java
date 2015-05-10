package Security;

/**
 * Created by Jableader on 10/5/2015.
 */
public interface Cipher {
    byte[] encrypt(byte[] input);
    byte[] decrypt(byte[] input);
}
