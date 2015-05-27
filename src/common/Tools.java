package common;

import security.Cipher;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.util.Base64;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public final class Tools {

    public static String toBase64(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }

    public static byte[] fromBase64(String s) {
        return Base64.getDecoder().decode(s);
    }

    public static Date getDate(JsonObject jso, String s) {
        return new Date(jso.getJsonNumber(s).longValue());
    }

    public static Date millisFromNow(long millis) {
        return new Date(new Date().getTime() + millis);
    }

    public static JsonObject decipherJsonObject(Cipher c, String s) {
        InputStream byteStream = new ByteArrayInputStream(fromBase64(s));
        InputStream decipheredStream = c.getDecipheringStream(byteStream);

        return Json.createReader(decipheredStream).readObject();
    }
}
