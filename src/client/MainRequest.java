package client;

import common.Login;
import common.Services;
import logging.Logger;
import security.Cipher;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class MainRequest extends Request {
    private final String command;
    private final String ticket;
    private final Cipher sessionCipher;
    private final Login login;


    public MainRequest(Logger logger, int port, String command, String ticket, Cipher sessionCipher, Login login) {
        super(logger, port, Services.ServerService);
        this.ticket = ticket;
        this.command = command;
        this.sessionCipher = sessionCipher;
        this.login = login;
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Json.createGenerator(sessionCipher.getCipheringStream(bos))
                .writeStartObject()
                    .write("command", command)
                    .write("id", login.id)
                    .write()

        return super.getJsonRequest()
                .add("ticket", ticket)
                .add("request", );
    }
}
