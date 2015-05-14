package server.internals;

import logging.Logger;
import server.RType;
import server.Request;
import server.Response;
import server.management.Login;

import java.net.InetAddress;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class InvalidRequest extends Request{
    protected InvalidRequest(Logger logger, Login login, Date expiry, InetAddress clientAddress) {
        super(logger, login, expiry, clientAddress);
    }

    @Override
    public RType getType() {
        return RType.Invalid;
    }

    @Override
    public Response getResponse() {
        return new InvalidResponse(logger, this, new Date());
    }
}
