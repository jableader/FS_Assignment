package server.ss;

import logging.Logger;
import security.Cipher;
import server.Response;

import javax.json.JsonObjectBuilder;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * This is simply a dummy service that removes every vowel from the string it recieves. In reality such a service would
 * not need this level of security, but it is simple to see how it could be swapped out for something that say, executes
 * arbitrary commands on the OS, which would need such security.
 */
public class ServiceTicketRequest extends Response {
    final Cipher sessionCipher;
    final Cipher serviceSecretCipher;

    public ServiceTicketRequest(Logger logger, Date dateCreated, Cipher sessionCipher, Cipher serviceSecretCipher) {
        super(logger, dateCreated);
        this.sessionCipher = sessionCipher;
        this.serviceSecretCipher = serviceSecretCipher;
    }

    @Override
    protected JsonObjectBuilder getJsonResponse() {
        return super.getJsonResponse()
                .add("result", result)
                .add("difference", recievedString.length() - result.length());
    }

    @Override
    public boolean wasSuccess() {
        return true;
    }
}
