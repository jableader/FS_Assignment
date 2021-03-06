package common;

import logging.Logger;
import security.AggregateCipher;
import security.Cipher;
import security.LoggingAggregateCipher;
import security.implementations.SwapNibbles;
import security.implementations.XorWithKey;
import security.implementations.XorWithPreviousBlock;

import javax.json.Json;
import javax.json.JsonException;
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

    public static Date secondsFromNow(long seconds) {
        return new Date(new Date().getTime() + seconds * 1000);
    }

    public static JsonObject decipherJsonObject(Cipher c, String s) throws IllegalArgumentException {
        InputStream byteStream = new ByteArrayInputStream(fromBase64(s));
        InputStream decipheredStream = c.getDecipheringStream(byteStream);

        try {
            return Json.createReader(decipheredStream).readObject();
        } catch (JsonException jsex) {
            throw new IllegalArgumentException("Could not decrypt with the given cipher", jsex);
        }
    }

    public static Cipher cipherForUseBetweenClientAndServer(byte[] key, Logger logger) {
        Cipher[] ciphers = new Cipher[] {
                new XorWithKey(key),
                new XorWithPreviousBlock("pretend I was generated elsewhere".getBytes()),
                new SwapNibbles()
        };

        return logger == null ? new AggregateCipher(ciphers) : new LoggingAggregateCipher(logger, ciphers);
    }
}
