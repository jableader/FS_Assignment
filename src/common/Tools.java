package common;

import javax.json.JsonObject;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Jableader on 11/05/2015.
 */
public final class Tools {

    public static String toHexString(byte[] bs){
        return new BigInteger(bs).toString(16);
    }

    public static byte[] fromHexString(String s) {
        return new BigInteger(s, 16).toByteArray();
    }

    public static Date getDate(JsonObject jso, String s) {
        return new Date(jso.getJsonNumber(s).longValue());
    }
}
