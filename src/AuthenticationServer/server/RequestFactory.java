package AuthenticationServer.server;

import AuthenticationServer.Key;
import AuthenticationServer.Login;
import AuthenticationServer.LoginManager;
import Logging.LogType;
import Logging.Logger;
import Security.Cipher;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;

import static Common.Tools.toHexString;

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
