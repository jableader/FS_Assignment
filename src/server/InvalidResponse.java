package server;

import logging.Logger;

import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class InvalidResponse extends Response {
    public InvalidResponse(Logger logger, Date timeCreated) {
        super(logger, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        return false;
    }
}
