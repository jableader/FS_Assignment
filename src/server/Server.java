package server;

import logging.LogType;
import logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class Server {
    final Map<String, RequestHandler> responses = new HashMap<>();
    final Logger logger;
    final RequestHandler invalidRequestHandler;

    public Server(Logger logger) {
        this.logger = logger;
        this.invalidRequestHandler = (source, req) -> new InvalidResponse(this.logger, new Date());
    }

    public void Serve(AtomicBoolean shouldStop, int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        logger.Log(LogType.Standard, "Awaiting connection");

        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        while (!shouldStop.get()) {
            Socket s = serverSocket.accept();

            threadPool.execute(() -> {
                try {
                    ServeSocket(s);
                } catch (IOException ex) {
                    logger.Log(LogType.Warning, "Something bad happened with a socket. Life moves on");
                } catch (Exception ex) {
                    logger.Log(LogType.Error, "Unexpected exception: " + ex.getMessage(), ex);

                    shouldStop.set(true);
                }
            });
        }

        threadPool.shutdown();
    }

    public void ServeSocket(Socket s) throws IOException {
        try {
            logger.Log(LogType.Standard, "Connected to " + s.getInetAddress().getHostAddress());

            JsonObject jso = Json.createReader(s.getInputStream()).readObject();

            logger.Log(LogType.Verbose, "Received: " + jso);

            Response response = responses
                    .getOrDefault(jso.getString("serviceName"), invalidRequestHandler)
                    .getResponse(s.getInetAddress(), jso);

            response.writeResponse(s.getOutputStream());
        } finally {
            s.close();
            logger.Log(LogType.Standard, "Connection with " + s.getInetAddress().getHostAddress() + " has been closed");
        }
    }

    public void registerRequest(String serviceName, RequestHandler rg) {
        responses.put(serviceName, rg);
    }
}
