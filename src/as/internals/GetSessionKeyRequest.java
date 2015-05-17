package as.internals;

import logging.Logger;
import server.Request;
import server.Response;
import server.management.Login;

import java.net.InetAddress;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class GetSessionKeyRequest extends Request {

    public GetSessionKeyRequest(Logger logger, Login login, InetAddress clientAddress) {
        super(logger, clientAddress);
    }

    @Override
    public Response getResponse() {
        return null;
    }
}
