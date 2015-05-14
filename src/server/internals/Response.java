package server.internals;

import server.management.Login;
import logging.LogType;
import logging.Logger;

import javax.json.*;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Response {
    protected final Logger logger;
    protected final Request request;
    protected final Date timeCreated;
    protected final RType type;

    protected Response(Logger logger, Request request, Date timeCreated, RType type) {
        this.logger = logger;
        this.request = request;
        this.timeCreated = timeCreated;
        this.type = type;

        logger.Log(LogType.Verbose, "Generating response of type " + type);
    }

    public boolean wasSuccess() {
        return request.login != null && type != RType.Invalid;
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
