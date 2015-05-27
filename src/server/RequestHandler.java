package server;

import javax.json.JsonObject;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public interface RequestHandler {
    Response getResponse(InetAddress source, JsonObject req);
}
