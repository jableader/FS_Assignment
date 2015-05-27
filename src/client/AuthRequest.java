package client;

import common.Login;
import common.Services;
import logging.Logger;
import security.implementations.XorWithKey;
import security.StreamCipher;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static common.Tools.fromBase64;
import static common.Tools.millisFromNow;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class AuthRequest extends Request {
    private String tgt;
    private byte[] keyForTgsSession;
    private Login login;

    public AuthRequest(Logger logger, int port, Login login) {
        super(logger, port, Services.Authentication);
        this.login = login;
    }

    @Override
    protected void processResponse(JsonObject jsonObject) {
        super.processResponse(jsonObject);

        if (wasSuccess()) {
            tgt = jsonObject.getString("tgt");

            String hexTgsKey = jsonObject.getJsonObject("sessionKey").getString("key");
            keyForTgsSession = fromBase64(hexTgsKey);
        }
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        return super.getJsonRequest()
                .add("expiry", millisFromNow(100 * 60 * 60).getTime())
                .add("id", login.id);
    }

    public StreamCipher getTgsCipher() {
        return new XorWithKey(keyForTgsSession);
    }

    public String getTgt() {
        return tgt;
    }
}
