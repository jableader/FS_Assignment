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
}
