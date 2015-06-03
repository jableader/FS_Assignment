package client;

import common.CommandLineArgs;
import common.Login;
import logging.LogType;
import logging.Logger;
import logging.StreamLogger;
import security.Cipher;

import java.io.IOException;
import java.util.Scanner;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class MMain {
    public static void main(String[] sargs)  {
        CommandLineArgs args = new CommandLineArgs(sargs);
        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"), args.hasKey("c"));

        if (!args.hasKey("l") && !args.hasKey("p"))
            logger.Log(LogType.Warning, "You have not provided a login and password, therefore 'bob' and 'password123' will be used");

        Login login = new Login(args.getString("l", "bob"), args.getString("p", "password123").getBytes());

        int authPort = args.getInt("asPort", args.getInt("port", 8888));
        AuthRequest authRequest = new AuthRequest(logger, authPort, login);

        authRequest.executeRequest();
        if (!authRequest.wasSuccess()) {
            logger.Log(LogType.Error, "Auth failed");
            return;
        }

        int tgsPort = args.getInt("tgsPort", args.getInt("port", 8888));
        TgsRequest tgsRequest = new TgsRequest(logger, tgsPort, login, authRequest.getTgsCipher(), authRequest.getTgt());
        tgsRequest.executeRequest();
        if (!tgsRequest.wasSuccess()) {
            logger.Log(LogType.Error, "Tgs request failed");
            return;
        }

        doStuffSecurely(logger, args, login, tgsRequest.getSessionCipher(), tgsRequest.getClientTicket());
    }

    static void doStuffSecurely(Logger logger, CommandLineArgs args, Login login, Cipher sessionCipher, String ticket) {
        int mainPort = args.getInt("mport", args.getInt("port", 8888));

        logger.Log(LogType.Standard, "Now you have been authenticated. Enter lines of text to see how the server responds");

        Scanner sc = new Scanner(System.in);
        String line;
        while (!(line = sc.nextLine()).isEmpty()) {
            MainRequest request = new MainRequest(logger, mainPort, line, ticket, sessionCipher, login);
            request.executeRequest();
            if (request.wasSuccess()) {
                logger.Log(LogType.Standard, "Received back " + request.getResult());
            } else {
                logger.Log(LogType.Warning, "The request was not successful");
            }
        }
    }
}