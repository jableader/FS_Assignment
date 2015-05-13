package Common;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Jableader on 11/05/2015.
 */
public class Tools {

    public static String toHexString(byte[] bs){
        return new BigInteger(bs).toString(16);
    }

    public static byte[] toByteArray(Date d) {
        byte[] b = new byte[Long.SIZE];

        long ld = d.getTime();
        for (int i = Long.SIZE - 1; i >= 0; i--) {
            b[i] = (byte)(0x0F & (ld << (8*i)));
        }

        return b;
    }
}
