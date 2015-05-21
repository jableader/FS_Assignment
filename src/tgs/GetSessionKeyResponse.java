package tgs;

import logging.LogType;
import logging.Logger;
import server.Response;

import java.util.Date;

/**
 * Created by Jableader on 14/5/2015.
 */
public class GetSessionKeyResponse extends Response {
    public GetSessionKeyResponse(Logger logger, Date timeCreated) {
        super(logger, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        logger.Log(LogType.Error, "Not ready");
        return false;
    }
}
