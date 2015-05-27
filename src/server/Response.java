package server;

import logging.LogType;
import logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public abstract class Response {
    protected final Logger logger;
    protected final Date timeCreated;

    protected Response(Logger logger, Date timeCreated) {
        this.logger = logger;
        this.timeCreated = timeCreated;
    }

    public abstract boolean wasSuccess();

    protected JsonObjectBuilder getJsonResponse() {
        logger.Log(LogType.Verbose, "Begin generating response of " + this.getClass().getName());

        return Json.createObjectBuilder()
                .add("success", wasSuccess());
    }

    public final void writeResponse(OutputStream s) throws IOException {
        JsonObject jsonResponse = getJsonResponse().build();

        logger.Log(LogType.Standard, "Generated: " + jsonResponse);

        Json.createWriter(s).write(jsonResponse);
        s.close();
    }
}
