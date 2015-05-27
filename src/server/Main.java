package server;

import common.CommandLineArgs;
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
import java.util.stream.Stream;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class Main {
    public static void main(String[] sargs) throws IOException {
        CommandLineArgs args = new CommandLineArgs(sargs);
        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"));
        Cipher serviceServerSecretCipher = args.hasKey("c") ?
                new LoggingAggregateCipher(logger, getServiceServerCipherChain()) :
                new AggregateCipher(getServiceServerCipherChain());

        Cipher tgsSecretCipher = args.hasKey("c") ?
                new LoggingAggregateCipher(logger, getTgsSecretCipherChain()) :
                new AggregateCipher(getTgsSecretCipherChain());

        Server server = new Server(logger);

        if (args.hasKey("as"))
            server.registerRequest(Services.Authentication.id, new AuthenticationHandler(logger, tgsSecretCipher));

        if (args.hasKey("tgs"))
            server.registerRequest(Services.TicketGranting.id, new TicketGrantingServiceHandler(logger, tgsSecretCipher, serviceServerSecretCipher));

        if (args.hasKey("ss"))
            server.registerRequest(Services.ServerService.id, new ServiceRequestHandler(logger, serviceServerSecretCipher));

        if (args.hasKey("?") || !Stream.of("as", "tgs", "ss").anyMatch(args::hasKey)) {
            printHelp();
            return;
        }

        server.Serve(new AtomicBoolean(false), args.getInt("port", 8888));
    }

    private static void printHelp() {
        System.out.println("Kerberos Server");
        System.out.println("Jacob Dunk,\t11654718");
        System.out.println();
        System.out.println("Run with any of the following parameters");
        System.out.println("Services to run");
        System.out.println("\t-as  \tRun the authentication service");
        System.out.println("\t-tgs \tRun the ticket granting service");
        System.out.println("\t-ss  \tRun the goal service");
        System.out.println();
        System.out.println("The following parameters may also be used");
        System.out.println("\t-v   \tVerbose logging");
        System.out.println("\t-c   \tPrint each step in ciphering");
        System.out.println("\t-port\tThe port to use");
        System.out.println();
        System.out.println("Usage: ");
        System.out.println("[-as] [-tgs] [-ss] [-v] [-port 1234]");
        System.out.println();
        System.out.println("Don't forget to add logins to the login.txt file");
        System.out.println("If logins.txt is left blank then the user 'bob' with password 'password123' will be used");
    }

    static Cipher[] getServiceServerCipherChain() {
        //Use hard coded keys for examples sake, in reality the keys would be much larger, randomly generated and securely
        //Distributed between the service

        byte[] key = "OMG so secure!!1!1!!".getBytes();
        byte[] initialisationVector = "Pretend this got generated elsewhere instead of being a code constant".getBytes();


        return new Cipher[]{
                new SwapNibbles(),
                new XorWithKey(key),
                new XorWithPreviousBlock(initialisationVector),
                new RotateBytesInBlock(key.length % initialisationVector.length, initialisationVector.length)
        };
    }

    static Cipher[] getTgsSecretCipherChain() {
        //Use hard coded keys for examples sake, in reality the keys would be much larger, randomly generated and securely
        //Distributed between the services

        byte[] key = "Ziggy played guitar!".getBytes();
        byte[] initialisationVector = "A different init vector to the last".getBytes();

        return new Cipher[]{
                new SwapNibbles(),
                new XorWithKey(key),
                new XorWithPreviousBlock(initialisationVector)
        };
    }
}
