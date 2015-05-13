package Server_Temp.internals;

import Server_Temp.management.LoginManager;
import Logging.LogType;
import Logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class RequestFactory {
    final LoginManager loginManager;
    final Logger logger;

    public RequestFactory(LoginManager loginManager, Logger logger) {
        this.loginManager = loginManager;
        this.logger = logger;
    }

    public Request getRequest(InputStream stream) {
        JsonObject json = Json.createReader(stream).readObject();

        Request request =  new Request(loginManager.getLogin(json.getString("id")),
                new Date(json.getJsonNumber("expiry").longValue()),
                RType.fromCode(json.getString("rtype"))
        );

        logger.Log(LogType.Standard, "Generating request from stream", request);

        return request;
    }

}
