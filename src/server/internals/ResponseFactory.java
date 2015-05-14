package server.internals;

import server.management.KeyManager;
import server.management.LoginManager;
import logging.Logger;
import security.Cipher;

import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class ResponseFactory {
    private final Cipher tgsCipher;
    private final Cipher sessionCipher;
    private final LoginManager loginManager;
    private final KeyManager keyManager;
    private final Logger logger;

    public ResponseFactory(Cipher tgsCipher, Cipher sessionCipher, LoginManager loginManager, KeyManager keyManager, Logger logger) {
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
        this.loginManager = loginManager;
        this.keyManager = keyManager;
        this.logger = logger;
    }

    public Response getResponse(Request rq) {
        switch (rq.getType()) {
            case GetTicketGrantingTicket:
                return new GetTgtResponse(logger, rq, keyManager, new Date(), rq.expiryDate, tgsCipher, sessionCipher);

            case GetSessionKey:
                return new GetSessionKeyResponse(logger, rq, new Date());

            default:
                return new Response(logger, rq, new Date(), RType.Invalid);
        }
    }

}