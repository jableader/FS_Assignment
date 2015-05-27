package server.ss;

import logging.Logger;
import security.Cipher;
import server.Response;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import static common.Tools.toBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 *
 * This is simply a dummy service that removes every vowel from the string it recieves. In reality such a service would
 * not need this level of security, but it is simple to see how it could be swapped out for something that say, executes
 * arbitrary commands on the OS, which would need such security.
 */
public class ServiceResponse extends Response {
    final Cipher sessionCipher;
    final String recievedString;
    final Date requestTimeStamp;

    public ServiceResponse(Logger logger, Date dateCreated, Cipher sessionCipher, String recievedString, Date requestTimeStamp) {
        super(logger, dateCreated);
        this.sessionCipher = sessionCipher;
        this.recievedString = recievedString;
        this.requestTimeStamp = requestTimeStamp;
    }

    @Override
    protected JsonObjectBuilder getJsonResponse() {
        String result = recievedString.replaceAll("[AEIOUaeiou]", "");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Json.createGenerator(sessionCipher.getCipheringStream(bos))
                .writeStartObject()
                    .write("result", result)
                    .write("time", requestTimeStamp.getTime() + 1)
                .writeEnd()
                .close();

        return super.getJsonResponse()
                .add("response", toBase64(bos.toByteArray()));
    }

    @Override
    public boolean wasSuccess() {
        return true;
    }
}
