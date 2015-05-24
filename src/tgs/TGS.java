package tgs;

import common.CommandLineParser;
import logging.LogType;
import logging.Logger;
import logging.StreamLogger;
import security.BasicCipher;
import security.Cipher;
import server.Request;
import server.Response;
import server.ResponseGenerator;

import javax.json.JsonObject;
import java.io.*;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.*;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class TGS implements ResponseGenerator {

    public static void main(String[] argsArray) throws IOException {
        CommandLineParser args = new CommandLineParser(argsArray);
        byte[] serviceKey = readBytes(new File(args.getString("sKey", "key.dat")));
        byte[] tgsKey = readBytes(new File(args.getString("key", "tgs.dat"));

        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"));

    }

    final byte[] serviceKey;
    final Cipher tgsCipher;
    final Logger logger;

    public TGS(Logger logger, byte[] serviceKey, Cipher tgsKey) {
        this.serviceKey = serviceKey;
        this.logger = logger;
        this.tgsCipher = tgsKey;
    }

    @Override
    public Response getResponse(InetAddress source, JsonObject req) {
        JsonObject tgs = decipherJsonObject(tgsCipher, req.getString("tgs"));
        Cipher clientSessionCiper = new BasicCipher(fromHexString(tgs.getString("key")));
        JsonObject authenticator = decipherJsonObject(clientSessionCiper, req.getString("authenticator"));

        boolean isDifferentAddress = !tgs.getString("clientAddress").equals(source.toString());
        boolean isDifferentId = !tgs.getString("id").equals(authenticator.getString("id"));
        boolean hasExpired = getDate(tgs, "expiry").before(new Date());

        if (isDifferentAddress || isDifferentId || hasExpired) {
            logger.Log(LogType.Warning, String.format("The TGS request was not valid. Expired: %b, Wrong Address: %b, Wrong ID: %b", hasExpired, isDifferentAddress, isDifferentId));

            return new UnsuccessfulResponse(logger, new Date());
        } else {


        }
    }
}
