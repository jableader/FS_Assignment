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
 * Created by Jableader on 10/5/2015.
 */
public class Server {
    final Map<String, ResponseGenerator> responses = new HashMap<>();
    final Logger logger;
    final ResponseGenerator invalidResponseGenerator = (source, req) -> new InvalidResponse(this.logger, new Date());

    public Server(Logger logger) {
        this.logger = logger;
    }

    public void Serve(AtomicBoolean shouldStop, int port) throws IOException {
        logger.Log(LogType.Standard, "Starting server");
        ServerSocket serverSocket = new ServerSocket(port);

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
        logger.Log(LogType.Standard, "Connected to " + s.getInetAddress().getHostAddress());

        JsonObject jso = Json.createReader(s.getInputStream()).readObject();

        logger.Log(LogType.Verbose, "Received: " + jso.toString());

        Response response = responses
                .getOrDefault(jso.getString("serviceName"), invalidResponseGenerator)
                .getResponse(s.getInetAddress(), jso);

        response.writeResponse(s.getOutputStream());

        s.close();
        logger.Log(LogType.Standard, "Finished with " + s.getInetAddress().getHostAddress());
    }

    public void registerRequest(String serviceName, ResponseGenerator rg) {
        responses.put(serviceName, rg);
    }
}
