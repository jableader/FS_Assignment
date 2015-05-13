package AuthenticationServer.server;

import AuthenticationServer.Key;
import AuthenticationServer.Login;
import Logging.LogType;
import Logging.Logger;
import Security.Cipher;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

import static Common.Tools.*;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Response {
    protected final Logger logger;
    protected final Login login;
    protected final Date timeCreated;

    public RType getType() {
        return RType.Invalid;
    }

    protected Response(Logger logger, Login login, Date timeCreated) {
        this.logger = logger;
        this.login = login;
        this.timeCreated = timeCreated;

        logger.Log(LogType.Verbose, "Generating response of type " + getType());
    }

    public boolean wasSuccess() {
        return login != null && getType() != RType.Invalid;
    }

    protected JsonObjectBuilder getJsonResponse() {
        logger.Log(LogType.Verbose, "Begin generating response");

        return Json.createObjectBuilder()
                .add("success", wasSuccess());
    }

   public void writeResponse(OutputStream s) {
       JsonObject jsonResponse = getJsonResponse().build();

       logger.Log(LogType.Standard, "Writing response to stream", jsonResponse);

       Json.createWriter(s).write(jsonResponse);
    }
}
