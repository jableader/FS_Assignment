package client;

import common.Login;
import common.Services;
import logging.Logger;
import security.implementations.XorWithKey;
import security.StreamCipher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static common.Tools.fromBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class TgsRequest extends Request {
    private final Login login;
    private final StreamCipher sessionCipher;
    private final String tgt;

    private String clientTicket;
    private byte[] serviceSessionKey;

    public TgsRequest(Logger logger, int port, Login login, StreamCipher sessionCipher, String tgt) {
        super(logger, port, Services.TicketGranting);
        this.login = login;
        this.sessionCipher = sessionCipher;
        this.tgt = tgt;
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        return super.getJsonRequest()
                .add("tgt", tgt)
                .add("authenticator", Json.createObjectBuilder()
                        .add("id", login.id));
    }

    @Override
    protected void processResponse(JsonObject response) {
        super.processResponse(response);

        clientTicket = response.getString("clientTicket");
        serviceSessionKey = sessionCipher.decryptBytes(fromBase64(response.getString("key")));
    }

    public String getClientTicket() {
        return clientTicket;
    }

    public StreamCipher getSessionCipher() {
        return new XorWithKey(serviceSessionKey);
    }
}
