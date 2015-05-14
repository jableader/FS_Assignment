package server.internals;

import logging.LogType;
import logging.Logger;
import server.Request;
import server.Response;

import java.util.Date;

/**
 * Created by Jableader on 14/5/2015.
 */
public class GetSessionKeyResponse extends Response {
    public GetSessionKeyResponse(Logger logger, Request request, Date timeCreated) {
        super(logger, request, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        logger.Log(LogType.Error, "Not ready");
        return false;
    }
}
