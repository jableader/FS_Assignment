package AuthenticationServer.server;

import AuthenticationServer.LoginManager;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class RequestFactory {
    final LoginManager loginManager;

    public RequestFactory(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public Request getRequest(InputStream stream) {
        JsonObject json = Json.createReader(stream).readObject();

        return new Request(loginManager.getLogin(json.getString("id")), new Date(), json.getString("serviceName"));
    }
}
