package server.as;

import common.*;
import logging.Logger;
import logging.PrefixedLogger;
import security.Cipher;
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
    private final Logger logger;
    private final LoginManager loginManager;
    private final KeyManager keyManager;
    private final Cipher tgsCipher;

    public AuthenticationHandler(Logger logger, Cipher tgsCipher) throws IOException {
        this.tgsCipher = tgsCipher;
        this.logger = new PrefixedLogger(logger, "AS: ");
        keyManager = new KeyManager(this.logger);
        loginManager = new LoginManager(this.logger, new File("logins.txt"));
    }

    @Override
    public Response getResponse(InetAddress address, JsonObject jsonRequest) {
        return new AuthenticationServiceResponse(
                logger,
                loginManager.getLogin(jsonRequest.getString("id")),
                address,
                tgsCipher, keyManager,
                new Date(),
                getDate(jsonRequest, "expiry")
        );
    }
}
