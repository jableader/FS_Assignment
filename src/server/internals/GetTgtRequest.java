package server.internals;

import logging.Logger;
import security.Cipher;
import server.RType;
import server.Request;
import server.Response;
import server.management.KeyManager;
import server.management.Login;

import java.net.InetAddress;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class GetTgtRequest extends Request {
    final Date keyExpirey;
    final KeyManager keyManager;
    final Cipher tgsCipher;
    final Cipher sessionCipher;

    public GetTgtRequest(Logger logger, Login login, InetAddress clientAddress, Date keyExpirey, KeyManager keyManager, Cipher tgsCipher, Cipher sessionCipher) {
        super(logger, login, clientAddress);

        this.keyExpirey = keyExpirey;
        this.keyManager = keyManager;
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
    }


    @Override
    public RType getType() {
        return RType.GetTicketGrantingTicket;
    }

    @Override
    public Response getResponse() {
        return new GetTgtResponse(logger, this, keyManager, new Date(), keyExpirey, tgsCipher, sessionCipher);
    }
}
