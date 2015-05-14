package server;

import logging.Logger;
import server.management.Login;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public abstract class Request {
    public final Login login;
    public final InetAddress clientAddress;

    protected final Logger logger;

    protected Request(Logger logger, Login login, InetAddress clientAddress) {
        this.logger = logger;
        this.login = login;
        this.clientAddress = clientAddress;
    }

    public abstract RType getType();
    public abstract Response getResponse();
}
