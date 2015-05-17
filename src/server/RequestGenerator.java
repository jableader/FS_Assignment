package server;

import javax.json.JsonObject;
import java.net.InetAddress;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public interface RequestGenerator {
    Request getRequest(InetAddress source, JsonObject req);
}
