package server;

import logging.Logger;

import java.net.InetAddress;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
class InvalidRequest extends Request{
    public final String serviceName;

    protected InvalidRequest(Logger logger, InetAddress clientAddress, String serviceName) {
        super(logger, clientAddress);

        this.serviceName = serviceName;
    }

    @Override
    public Response getResponse() {
        return new InvalidResponse(logger, new Date());
    }
}
