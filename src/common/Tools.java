package common;

import security.Cipher;
import sun.misc.IOUtils;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
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

    public static byte[] readBytes(File f) throws IOException {
        InputStream fs = null;
        try {
            fs = new FileInputStream(f);
            return IOUtils.readFully(fs, 0, true);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
    }

    public static JsonObject decipherJsonObject(Cipher c, String s) {
        InputStream byteStream = new ByteArrayInputStream(fromHexString(s));
        InputStream decipheredStream = c.getDecipheringStream(byteStream);

        return Json.createReader(decipheredStream).readObject();
    }
}
