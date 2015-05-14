package server.internals;

import server.management.LoginManager;
import logging.LogType;
import logging.Logger;

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

    public final Request getRequest(InputStream stream) {
        Request request = getRequest(Json.createReader(stream).readObject());

        logger.Log(LogType.Standard, "Generating request from stream", request);

        return request;
    }

    protected Request getRequest(JsonObject json) {
        return new Request(loginManager.getLogin(json.getString("id")),
                new Date(json.getJsonNumber("expiry").longValue()),
                RType.fromCode(json.getString("rtype")),
                clientAddress);
    }

}
