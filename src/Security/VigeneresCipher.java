package Security;

import java.util.Arrays;

/** Implementation of a simple polyalphabetic cipher
 * Created by Jableader on 10/5/2015.
 */
public class VigeneresCipher implements Cipher {
    final byte[] key;

    public VigeneresCipher(byte[] key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] plainText) {
        byte[] cipherText = Arrays.copyOf(plainText, plainText.length);

        for (int i = 0; i < cipherText.length; i++)
            cipherText[i] += key[i % key.length];

        return cipherText;
    }

    public byte[] decrypt(byte[] cipherText){
        byte[] plainText = Arrays.copyOf(cipherText, cipherText.length);

        for (int i = 0; i < cipherText.length; i++)
            plainText[i] -= key[i % key.length];

        return plainText;
    }
}
