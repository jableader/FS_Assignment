package AuthenticationServer;

import AuthenticationServer.server.Request;
import AuthenticationServer.server.RequestFactory;
import AuthenticationServer.server.Response;
import AuthenticationServer.server.ResponseFactory;
import Logging.LogType;
import Logging.Logger;
import Security.Cipher;
import Security.Hasher;

import javax.json.*;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import static Common.Tools.toHexString;

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

    ServerSocket serverSocket;
    public void Serve() throws IOException{
        logger.Log(LogType.Standard, "Starting Server");
        serverSocket = new ServerSocket(8888);
    }

    public void ServeSocket(Socket s) throws IOException {
        logger.Log(LogType.Standard, "Connected to " + s.getInetAddress().getHostAddress());

        Request request = requests.getRequest(s.getInputStream());
        Response response = responses.getResponse(request);

        response.writeResponse(s.getOutputStream());

        s.close();
    }
}
