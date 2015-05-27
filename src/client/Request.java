package client;

import common.Services;
import logging.LogType;
import logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.net.Socket;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class Request {
    private final Logger logger;
    private final int port;

    private boolean wasSuccess;

    public Request(Logger logger, int port, Services service) {
        this.logger = logger;
        this.port = port;
        this.service = service;

        logger.Log(LogType.Standard, "Performing " + service.name() + " request");

        try {
            processResponse(performRequest());
        } catch (IOException ex) {
            wasSuccess = false;
        }
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

        Json.createWriter(s.getOutputStream())
                .writeObject(getJsonRequest().build());

        return Json.createReader(s.getInputStream())
                .readObject();
    }

    private final Services service;

    public Services getService() {
        return service;
    }

    public boolean wasSuccess() {
        return wasSuccess;
    }
}
