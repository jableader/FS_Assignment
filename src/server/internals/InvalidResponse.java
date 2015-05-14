package server.internals;

import logging.Logger;
import server.Request;
import server.Response;

import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class InvalidResponse extends Response {

    protected InvalidResponse(Logger logger, Request request, Date timeCreated) {
        super(logger, request, timeCreated);
    }

    @Override
    public boolean wasSuccess() {
        return false;
    }
}
