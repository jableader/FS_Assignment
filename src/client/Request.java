package client;

import common.Services;
import logging.LogType;
import logging.Logger;
import security.Cipher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class Request {
    protected final Logger logger;
    protected final int port;

    private boolean wasSuccess;
    private boolean hasBeenProcessed;

    public Request(Logger logger, int port, Services service) {
        this.logger = logger;
        this.port = port;
        this.service = service;

        logger.Log(LogType.Standard, "Performing " + service.name() + " request");
    }

    protected void processResponse(JsonObject response) {
        logger.Log(LogType.Verbose, "Processing response " + response);

        wasSuccess = response.getBoolean("success");
    }

    protected JsonObjectBuilder getJsonRequest() {
        return Json.createObjectBuilder()
                .add("serviceName", service.id);
    }

    private JsonObject performRequest() throws IOException {
        Socket s = new Socket("localhost", port);

        OutputStream os = s.getOutputStream();
        Json.createWriter(os)
                .write(getJsonRequest().build());

        JsonObject response = Json.createReader(s.getInputStream())
                .readObject();

        s.close();
        return response;
    }

    private final Services service;

    public Services getService() {
        return service;
    }

    public boolean wasSuccess() {
        return wasSuccess;
    }

    public boolean hasBeenProcessed() {
        return hasBeenProcessed;
    }

    public void executeRequest() {
        try {
            processResponse(performRequest());
        } catch (IOException ex) {
            logger.Log(LogType.Error, "Failed to communicate with " + service + " service: " + ex.getMessage());
            wasSuccess = false;
        } finally {
            hasBeenProcessed = true;
        }
    }
}
