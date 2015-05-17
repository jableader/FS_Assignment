package as.internals;

import logging.Logger;
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
public class TgtRequest extends Request {
    final Date keyExpirey;
    final KeyManager keyManager;
    final Login login;

    public TgtRequest(Logger logger, InetAddress clientAddress, KeyManager keyManager, Date keyExpirey, Login login) {
        super(logger, clientAddress);

        this.keyExpirey = keyExpirey;
        this.keyManager = keyManager;
        this.login = login;
    }


    @Override
    public Response getResponse() {
        return new TgtResponse(logger, login, clientAddress, keyManager, new Date(), keyExpirey);
    }
}
