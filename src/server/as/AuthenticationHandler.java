package server.as;

import common.*;
import logging.Logger;
import logging.PrefixedLogger;
import server.RequestHandler;
import server.Response;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.getDate;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class AuthenticationHandler implements RequestHandler {
    final Logger logger;
    final LoginManager loginManager;
    final KeyManager keyManager;

    public AuthenticationHandler(Logger logger) throws IOException {
        this.logger = new PrefixedLogger(logger, "AS: ");
        keyManager = new KeyManager(this.logger);
        loginManager = new LoginManager(this.logger, new File("logins.txt"));
    }

    @Override
    public Response getResponse(InetAddress address, JsonObject jsonRequest) {
        return new TgtResponse(
                logger,
                loginManager.getLogin(jsonRequest.getString("id")),
                address,
                keyManager,
                new Date(),
                getDate(jsonRequest, "expiry")
        );
    }
}
