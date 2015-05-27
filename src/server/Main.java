package server;

import common.CommandLineParser;
import common.Services;
import logging.Logger;
import logging.StreamLogger;
import security.*;
import security.implementations.*;
import server.as.AuthenticationHandler;
import server.ss.ServiceRequestHandler;
import server.tgs.TicketGrantingServiceHandler;

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
        Cipher serviceServerSecretCipher = getServiceServerCipher();
        Cipher tgsSecretCipher = getTgsSecretCipher();

        Server server = new Server(logger);

        if (args.hasKey("as"))
            server.registerRequest(Services.Authentication.id, new AuthenticationHandler(logger, tgsSecretCipher));

        if (args.hasKey("tgs"))
            server.registerRequest(Services.TicketGranting.id, new TicketGrantingServiceHandler(logger, tgsSecretCipher, serviceServerSecretCipher));

        if (args.hasKey("ss"))
            server.registerRequest(Services.ServerService.id, new ServiceRequestHandler(logger, serviceServerSecretCipher));

        server.Serve(new AtomicBoolean(false), args.getInt("port", 8888));
    }

    static Cipher getServiceServerCipher() {
        //Use hard coded keys for examples sake, in reality the keys would be much larger, randomly generated and securely
        //Distributed between the service

        byte[] key = "OMG so secure!!1!1!!".getBytes();
        byte[] initialisationVector = "Pretend this got generated elsewhere instead of being a code constant".getBytes();
        int blockSize = initialisationVector.length;

        return new AggregateCipher(
                new XorWithPreviousBlock(initialisationVector),
                new RotateBytesInBlock(key.length, blockSize),
                new SwapNibbles(),
                new XorWithKey(key)
        );
    }

    static Cipher getTgsSecretCipher() {
        //Use hard coded keys for examples sake, in reality the keys would be much larger, randomly generated and securely
        //Distributed between the services

        byte[] key = "Ziggy played guitar!".getBytes();
        byte[] initialisationVector = "Pretend this got generated elsewhere instead of being a code constant".getBytes();

        return new AggregateCipher(
                new XorWithKey(key),
                new RotateBits(4),
                new SwapNibbles(),
                new XorWithPreviousBlock(initialisationVector)
        );
    }
}
