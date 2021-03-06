package server.ss;

import common.Tools;
import logging.LogType;
import logging.Logger;
import logging.PrefixedLogger;
import security.Cipher;
import security.implementations.XorWithKey;
import security.StreamCipher;
import server.InvalidResponse;
import server.RequestHandler;
import server.Response;

import javax.json.JsonObject;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.*;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class ServiceRequestHandler implements RequestHandler {
    final Logger logger;
    final Cipher secretCipher;

    public ServiceRequestHandler(Logger logger, Cipher secretCipher) {
        this.logger = new PrefixedLogger(logger, "SS: ");
        this.secretCipher = secretCipher;
    }

    @Override
    public Response getResponse(InetAddress source, JsonObject req) {
        JsonObject ticket = decipherJsonObject(secretCipher, req.getString("ticket"));
        Cipher sessionCipher = Tools.cipherForUseBetweenClientAndServer(fromBase64(ticket.getString("key")), logger);
        JsonObject request = decipherJsonObject(sessionCipher, req.getString("request"));

        Date expiry = getDate(ticket, "expiry");
        boolean hasExpired = expiry.before(new Date());
        boolean isDifferentIp = !source.toString().equals(ticket.getString("address"));
        boolean isDifferentId = !request.getString("id").equals(ticket.getString("id"));

        if (hasExpired || isDifferentId || isDifferentIp) {
            logger.Log(LogType.Error, "Invalid auth details. Expired: %b, WrongIp: %b, isDifferentId: %b", hasExpired, isDifferentIp, isDifferentId);
            return new InvalidResponse(logger, new Date());
        }

        return new ServiceResponse(logger, new Date(), sessionCipher, request.getString("command"), getDate(request, "time"));
    }
}
