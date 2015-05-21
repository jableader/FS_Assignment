package server;

import logging.Logger;
import server.Response;

import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
class InvalidResponse extends Response {
    protected InvalidResponse(Logger logger, Date timeCreated) {
        super(logger, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        return false;
    }
}
