package server;

import logging.LogType;
import logging.Logger;
import server.management.Login;

import javax.json.*;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
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

   public final void writeResponse(OutputStream s) {
       JsonObject jsonResponse = getJsonResponse().build();

       logger.Log(LogType.Standard, "Writing response to stream", jsonResponse);

       Json.createWriter(s).write(jsonResponse);
    }
}
