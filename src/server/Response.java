package server;

import logging.LogType;
import logging.Logger;

import javax.json.*;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public abstract class Response {
    protected final Logger logger;
    protected final Request request;
    protected final Date timeCreated;

    protected Response(Logger logger, Request request, Date timeCreated) {
        this.logger = logger;
        this.request = request;
        this.timeCreated = timeCreated;

        logger.Log(LogType.Verbose, "Generating response for type " + request.getType());
    }

    public boolean wasSuccess() {
        return request.login != null;
    }

    protected JsonObjectBuilder getJsonResponse() {
        logger.Log(LogType.Verbose, "Begin generating response");

        return Json.createObjectBuilder()
                .add("success", wasSuccess());
    }

   public final void writeResponse(OutputStream s) {
       JsonObject jsonResponse = getJsonResponse().build();

       logger.Log(LogType.Standard, "Writing response to stream", jsonResponse);

       Json.createWriter(s).write(jsonResponse);
    }
}
