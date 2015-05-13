package Server;

import Server.internals.Request;
import Server.internals.RequestFactory;
import Server.internals.Response;
import Server.internals.ResponseFactory;
import Logging.LogType;
import Logging.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Server {
    final Logger logger;
    final RequestFactory requests;
    final ResponseFactory responses;

    public Server(Logger logger, RequestFactory requests, ResponseFactory responses) {
        this.logger = logger;
        this.requests = requests;
        this.responses = responses;
    }

    public void Serve(AtomicBoolean shouldStop) throws IOException {
        logger.Log(LogType.Standard, "Starting server");
        ServerSocket serverSocket = new ServerSocket(8888);

        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        while (!shouldStop.get()) {
            Socket s = serverSocket.accept();

            threadPool.submit(() -> {
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

        Request request = requests.getRequest(s.getInputStream());
        Response response = responses.getResponse(request);

        response.writeResponse(s.getOutputStream());

        s.close();
    }
}
