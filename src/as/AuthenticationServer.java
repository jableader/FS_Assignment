package as;

import common.CommandLineParser;
import common.Services;
import logging.Logger;
import logging.StreamLogger;
import server.Request;
import server.Response;
import server.ResponseGenerator;
import common.KeyManager;
import as.management.Login;
import as.management.LoginManager;
import server.Server;

import javax.json.JsonObject;

import static common.Tools.getDate;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class AuthenticationServer implements ResponseGenerator {

    public static void main(String[] args) throws IOException {
        CommandLineParser clp = new CommandLineParser(args);

        Logger logger = new StreamLogger(System.out, System.err, clp.hasKey("v"));
        LoginManager lm = new LoginManager(logger, Arrays.asList(new Login("bob", "password123".getBytes())));
        KeyManager km = new KeyManager(logger);

        Server server = new Server(logger);

        server.registerRequest(Services.GetTicketGrantingTicket.id, new AuthenticationServer(logger, lm, km));
        server.Serve(new AtomicBoolean(false), clp.getInt("port", 8888));
    }

    final Logger logger;
    final LoginManager loginManager;
    final KeyManager keyManager;


    public AuthenticationServer(Logger logger, LoginManager loginManager, KeyManager keyManager) {
        this.logger = logger;
        this.loginManager = loginManager;
        this.keyManager = keyManager;
    }

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
