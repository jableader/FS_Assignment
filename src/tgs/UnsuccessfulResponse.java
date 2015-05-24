package tgs;

import logging.Logger;
import server.Response;

import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class UnsuccessfulResponse extends Response {
    protected UnsuccessfulResponse(Logger logger, Date timeCreated) {
        super(logger, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        return false;
    }
}
