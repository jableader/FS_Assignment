package client;

import common.Login;
import common.Services;
import logging.LogType;
import logging.Logger;
import security.Cipher;
import security.implementations.XorWithKey;
import security.StreamCipher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.io.ByteArrayOutputStream;

import static common.Tools.fromBase64;
import static common.Tools.toBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class TgsRequest extends Request {
    private final Login login;
    private final Cipher tgsSessionCipher;
    private final String tgt;

    private String clientTicket;
    private byte[] serviceSessionKey;

    public TgsRequest(Logger logger, int port, Login login, Cipher sessionCipher, String tgt) {
        super(logger, port, Services.TicketGranting);
        this.login = login;
        this.tgsSessionCipher = sessionCipher;
        this.tgt = tgt;
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Json.createGenerator(tgsSessionCipher.getCipheringStream(bos))
                .writeStartObject()
                .write("id", login.id)
                .writeEnd()
                .close();

        return super.getJsonRequest()
                .add("tgt", tgt)
                .add("authenticator", toBase64(bos.toByteArray()));
    }

    @Override
    protected void processResponse(JsonObject response) {
        super.processResponse(response);

        clientTicket = response.getString("clientTicket");

        serviceSessionKey = tgsSessionCipher.decryptBytes(fromBase64(response.getString("key")));
        logger.Log(LogType.Verbose, "Received serviceSessionKey " + toBase64(serviceSessionKey));
    }

    public String getClientTicket() {
        return clientTicket;
    }

    public StreamCipher getSessionCipher() {
        return new XorWithKey(serviceSessionKey);
    }
}
