package tgs;

import common.CommandLineParser;
import common.KeyManager;
import logging.LogType;
import logging.Logger;
import logging.StreamLogger;
import security.BasicCipher;
import security.Cipher;
import security.EmptyCipher;
import server.Response;
import server.ResponseGenerator;
import server.Server;

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
        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"));
        Cipher tgsCipher = new EmptyCipher();
        Cipher serviceCipher = new EmptyCipher();
        KeyManager keyManager = new KeyManager(logger);

        Server server = new Server(logger);
        server.registerRequest("tgs", new TGS(logger, keyManager, tgsCipher, serviceCipher));

    }

    final KeyManager keyManager;
    final Cipher tgsSecretCipher;
    final Cipher serviceSecretCipher;
    final Logger logger;

    public TGS(Logger logger, KeyManager keyManager, Cipher tgsKey, Cipher serviceSecretCipher) {
        this.keyManager = keyManager;
        this.logger = logger;
        this.tgsSecretCipher = tgsKey;
        this.serviceSecretCipher = serviceSecretCipher;
    }

    @Override
    public Response getResponse(InetAddress source, JsonObject req) {
        JsonObject tgt = decipherJsonObject(tgsSecretCipher, req.getString("tgt"));
        Cipher tgsSessionCipher = new BasicCipher(fromHexString(tgt.getString("key")));
        JsonObject authenticator = decipherJsonObject(tgsSessionCipher, req.getString("authenticator"));

        boolean isDifferentAddress = !tgt.getString("clientAddress").equals(source.toString());
        boolean isDifferentId = !tgt.getString("id").equals(authenticator.getString("id"));
        boolean hasExpired = getDate(tgt, "expiry").before(new Date());

        if (isDifferentAddress || isDifferentId || hasExpired) {
            logger.Log(LogType.Warning, String.format("The TGS request was not valid. Expired: %b, Wrong Address: %b, Wrong ID: %b", hasExpired, isDifferentAddress, isDifferentId));

            return new UnsuccessfulResponse(logger, new Date());
        } else {
            logger.Log(LogType.Verbose, "TGS Request verified, exchanging session key");

            return new GetSessionKeyResponse(logger, new Date(), keyManager, source, millisFromNow(1000*60*60), serviceSecretCipher, tgsSessionCipher, tgt.getString("id"));
        }
    }
}
