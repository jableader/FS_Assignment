package server;

import logging.Logger;

import java.net.InetAddress;

/**
 * Created by Jableader on 12/05/2015.
 */
public abstract class Request {
    public final InetAddress clientAddress;
    protected final Logger logger;

    protected Request(Logger logger, InetAddress clientAddress) {
        this.logger = logger;
        this.clientAddress = clientAddress;
    }

    public abstract Response getResponse();
}
