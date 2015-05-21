package tgs;

import server.Request;
import server.RequestGenerator;

import javax.json.JsonObject;
import java.net.InetAddress;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class TGS implements RequestGenerator {
    public static void main(String[] args) {

    }

    @Override
    public Request getRequest(InetAddress source, JsonObject req) {
        return null;
    }
}
