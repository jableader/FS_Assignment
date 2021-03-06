package server.tgs;

import common.KeyManager;
import common.Tools;
import logging.LogType;
import logging.Logger;
import logging.PrefixedLogger;
import security.Cipher;
import server.InvalidResponse;
import server.Response;
import server.RequestHandler;

import javax.json.JsonObject;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.*;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class TicketGrantingServiceHandler implements RequestHandler {
    final KeyManager keyManager;
    final Cipher tgsSecretCipher;
    final Cipher serviceSecretCipher;
    final Logger logger;

    public TicketGrantingServiceHandler(Logger logger, Cipher tgsSecretCipher, Cipher serviceSecretCipher) {
        this.logger = new PrefixedLogger(logger, "TGS: ");
        this.tgsSecretCipher = tgsSecretCipher;
        this.serviceSecretCipher = serviceSecretCipher;
        keyManager = new KeyManager(this.logger);
    }

    @Override
    public Response getResponse(InetAddress source, JsonObject req) {
        JsonObject tgt;
        tgt = decipherJsonObject(tgsSecretCipher, req.getString("tgt"));

        Cipher tgsSessionCipher = Tools.cipherForUseBetweenClientAndServer(fromBase64(tgt.getString("key")), logger);
        JsonObject authenticator = decipherJsonObject(tgsSessionCipher, req.getString("authenticator"));

        boolean isDifferentAddress = !tgt.getString("clientAddress").equals(source.toString());
        boolean isDifferentId = !tgt.getString("id").equals(authenticator.getString("id"));
        boolean hasExpired = getDate(tgt, "expiry").before(new Date());

        if (isDifferentAddress || isDifferentId || hasExpired) {
            logger.Log(LogType.Warning, String.format("The TGS request was not valid. Expired: %b, Wrong Address: %b, Wrong ID: %b", hasExpired, isDifferentAddress, isDifferentId));

            return new InvalidResponse(logger, new Date());
        } else {
            logger.Log(LogType.Verbose, "TGS Request verified, exchanging session key");

            return new TicketGrantingServiceResponse(logger, new Date(), keyManager, secondsFromNow(1000 * 60 * 60), serviceSecretCipher, tgsSessionCipher, tgt.getString("id"), source);
        }
    }
}
