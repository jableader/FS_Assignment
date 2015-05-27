package client;

import common.CommandLineParser;
import common.Login;
import logging.LogType;
import logging.Logger;
import logging.StreamLogger;

import java.io.IOException;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class main {
    public static void main(String[] sargs) throws IOException {
        CommandLineParser args = new CommandLineParser(sargs);
        Logger logger = new StreamLogger(System.out, System.err, args.hasKey("v"));

        if (!args.hasKey("l") && !args.hasKey("p")) {
            logger.Log(LogType.Error, "You must provide a login name and password");
            return;
        }

        Login login = new Login(args.getString("l"), args.getString("p").getBytes());

        int authPort = args.getInt("asPort", args.getInt("port", 8888));
        AuthRequest authRequest = new AuthRequest(logger, authPort, login);
        if (!authRequest.wasSuccess()) {
            logger.Log(LogType.Error, "Auth failed");
            return;
        }

        int tgsPort = args.getInt("tgsPort", args.getInt("port", 8888));
        TgsRequest tgsRequest = new TgsRequest(logger, tgsPort, login, authRequest.getTgsCipher(), authRequest.getTgt());
        if (!tgsRequest.wasSuccess()) {
            logger.Log(LogType.Error, "Tgs request failed");
            return;
        }

        logger.Log(LogType.Standard, "Auth was a success, you may now communicate freely with the server");

    }
}