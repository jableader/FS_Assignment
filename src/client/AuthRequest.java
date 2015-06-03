package client;

import common.Login;
import common.Services;
import common.Tools;
import logging.LogType;
import logging.Logger;
import security.AggregateCipher;
import security.Cipher;
import security.implementations.*;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static common.Tools.*;

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

            try {
                JsonObject tgsKey = decipherJsonObject(getLoginCipher(), jsonObject.getString("sessionKey"));

                String base64TgsKey = tgsKey.getString("key");
                logger.Log(LogType.Verbose, "Received TGS Session Key " + base64TgsKey);

                keyForTgsSession = fromBase64(base64TgsKey);
            } catch (IllegalArgumentException ex) {
                logger.Log(LogType.Error, "Could not decrypt the session key for the TGS");
            }
        }
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        return super.getJsonRequest()
                .add("expiry", secondsFromNow(100 * 60 * 60).getTime())
                .add("id", login.id);
    }

    @Override
    public boolean wasSuccess() {
        return super.wasSuccess() && !(hasBeenProcessed() && keyForTgsSession == null);
    }

    Cipher getLoginCipher() {
        return Tools.cipherForUseBetweenClientAndServer(login.password, logger);
    }

    public Cipher getTgsCipher() {
        return Tools.cipherForUseBetweenClientAndServer(keyForTgsSession, logger);
    }

    public String getTgt() {
        return tgt;
    }
}
