package server.ss;

import logging.LogType;
import logging.Logger;
import security.BasicCipher;
import security.Cipher;
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
public class ServiceTicketRequestHandler implements RequestHandler {
    final Logger logger;
    final Cipher secretCipher;

    public ServiceTicketRequestHandler(Logger logger, Cipher secretCipher) {
        this.logger = logger;
        this.secretCipher = secretCipher;
    }

    @Override
    public Response getResponse(InetAddress source, JsonObject req) {
        JsonObject ticket = decipherJsonObject(secretCipher, req.getString("ticket"));
        Cipher sessionCipher = new BasicCipher(fromBase64(ticket.getString("key")));
        JsonObject request = decipherJsonObject(sessionCipher, "request");

        boolean hasExpired = getDate(ticket, "expiry").after(new Date());
        boolean isDifferentIp = !source.toString().equals(ticket.getString("address"));
        boolean isDifferentId = !request.getString("id").equals(ticket.getString("id"));

        if (hasExpired || isDifferentId || isDifferentIp) {
            logger.Log(LogType.Error, "Invalid auth details. Expired: %b, WrongIp: %b, isDifferentId: %b");
            return new InvalidResponse(logger, new Date());
        }

        return new ServiceTicketRequest(logger, new Date(), sessionCipher, serviceSecretCipher);
    }
}
