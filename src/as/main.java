package as;

import common.RType;
import logging.Logger;
import logging.StreamLogger;
import server.Server;
import as.internals.GetSessionKeyRequest;
import as.internals.TgtRequest;
import server.management.KeyManager;
import server.management.Login;
import server.management.LoginManager;

import static common.Tools.getDate

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class main {

    public static void main(String[] args) throws IOException{
        Logger logger = new StreamLogger(System.out, System.err, true);
        LoginManager lm = new LoginManager(logger, Arrays.asList(new Login("bob", "password123".getBytes())));
        KeyManager km = new KeyManager(logger);

        Server s = new Server(logger);
        s.registerRequest(RType.GetTicketGrantingTicket.id,
                (address, jsonRequest) ->
                        new TgtRequest(logger, address, km, getDate(jsonRequest, "expiry"), lm.getLogin(jsonRequest.getString("id"))));

        s.registerRequest(RType.GetSessionKey.id,
                (address, jsonRequest) ->
                    new GetSessionKeyRequest(logger, lm.getLogin(jsonRequest.getString("id")), address));

        s.Serve(new AtomicBoolean(false), 8888);
    }
}
