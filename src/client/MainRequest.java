package client;

import common.Login;
import common.Services;
import logging.LogType;
import logging.Logger;
import security.Cipher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import static common.Tools.decipherJsonObject;
import static common.Tools.getDate;
import static common.Tools.toBase64;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class MainRequest extends Request {
    private final String command;
    private final String ticket;
    private final Cipher sessionCipher;
    private final Login login;
    private final Date timeStamp;

    private String result;
    private boolean recievedCorrectResponseTimestamp;

    public MainRequest(Logger logger, int port, String command, String ticket, Cipher sessionCipher, Login login) {
        super(logger, port, Services.ServerService);
        this.ticket = ticket;
        this.command = command;
        this.sessionCipher = sessionCipher;
        this.login = login;

        timeStamp = new Date();
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean wasSuccess() {
        return super.wasSuccess() && (!hasBeenProcessed() || recievedCorrectResponseTimestamp);
    }

    @Override
    protected JsonObjectBuilder getJsonRequest() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Json.createGenerator(sessionCipher.getCipheringStream(bos))
                .writeStartObject()
                    .write("command", command)
                    .write("id", login.id)
                    .write("time", timeStamp.getTime())
                .writeEnd()
                .close();

        return super.getJsonRequest()
                .add("ticket", ticket)
                .add("request", toBase64(bos.toByteArray()));
    }

    @Override
    protected void processResponse(JsonObject baseResponse) {
        super.processResponse(baseResponse);

        if (wasSuccess()) {
            JsonObject response;
            try {
                response = decipherJsonObject(sessionCipher, baseResponse.getString("response"));
            } catch (IllegalArgumentException ex) {
                logger.Log(LogType.Error, "Could not decipher the response");
                return;
            }

            result = response.getString("result");
            recievedCorrectResponseTimestamp = getDate(response, "time").getTime() - timeStamp.getTime() == 1;

            if (!recievedCorrectResponseTimestamp) {
                logger.Log(LogType.Warning, "Did not recieve the expected timestamp from the server. It can not be trusted.");
            }
        }
    }
}
