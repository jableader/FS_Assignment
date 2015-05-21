package common;

import javax.json.JsonObject;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by Jableader on 11/05/2015.
 */
public final class Tools {

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] fromHexString(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static Date getDate(JsonObject jso, String s) {
        return new Date(jso.getJsonNumber(s).longValue());
    }

    public static Date millisFromNow(long millis) {
        return new Date(new Date().getTime() + millis);
    }
}
