package server.internals;

import logging.Logger;
import server.RType;
import server.Request;
import server.Response;
import server.management.Login;

import java.net.InetAddress;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class GetSessionKeyRequest extends Request {


    protected GetSessionKeyRequest(Logger logger, Login login, InetAddress clientAddress) {
        super(logger, login, clientAddress);
    }

    @Override
    public RType getType() {
        return null;
    }

    @Override
    public Response getResponse() {
        return null;
    }
}
