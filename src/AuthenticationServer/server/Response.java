package AuthenticationServer.server;

import AuthenticationServer.Key;
import AuthenticationServer.Login;
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

    protected final Login login;
    protected final Date timeCreated;

    public RType getType() {
        return RType.Invalid;
    }

    protected Response(Login login, Date timeCreated) {
        this.login = login;
        this.timeCreated = timeCreated;
    }

    public boolean wasSuccess() {
        return login != null && getType() != RType.Invalid;
    }

    protected JsonObjectBuilder getJsonResponse() {
        return Json.createObjectBuilder()
                .add("success", wasSuccess());
    }

   public void writeResponse(OutputStream s) {
        Json.createWriter(s)
                .write(getJsonResponse().build());
    }
}
