package server;

import server.management.LoginManager;
import logging.LogType;
import logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.InputStream;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
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
        switch (RType.fromCode(json.getString("rtype"))) {

        }
    }
}
