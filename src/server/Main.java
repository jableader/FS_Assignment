package server;

import common.CommandLineParser;
import common.Services;
import logging.Logger;
import logging.StreamLogger;
import server.as.AuthenticationHandler;
import server.tgs.TicketGrantingService;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class Main {
    public static void main(String[] sargs) throws IOException {
        CommandLineParser args = new CommandLineParser(sargs);
        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"));

        Server server = new Server(logger);

        if (args.hasKey("as"))
            server.registerRequest(Services.Authentication.id, new AuthenticationHandler(logger));

        if (args.hasKey("tgs"))
            server.registerRequest(Services.TicketGranting.id, new TicketGrantingService(logger, null, null));

        server.Serve(new AtomicBoolean(false), args.getInt("port", 8888));
    }
}
