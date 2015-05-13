package AuthenticationServer;

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
    final LoginManager logins;
    final KeyManager keyManager;
    final Cipher cipher;

    public Server(Logger logger, LoginManager manager, KeyManager keyManager, Cipher cipher) {
        this.logins = manager;
        this.logger = logger;
        this.keyManager = keyManager;
        this.cipher = cipher;

        logger.Log(LogType.Verbose, "Logger initialised");
    }

    ServerSocket serverSocket;
    public void Serve() throws IOException{
        logger.Log(LogType.Standard, "Starting Server");
        serverSocket = new ServerSocket(8888);
    }

    public void ServeSocket(Socket s) throws IOException{
        logger.Log(LogType.Standard, "Connected to " + s.getInetAddress().getHostAddress());

        logger.Log(LogType.Verbose, "Parsing");

        JsonObject request = Json.createReader(s.getInputStream()).readObject();

        String userId = request.getString("id");
        logger.Log(LogType.Standard, "Looking up user " + userId);

        Login login = logins.getLogin(userId);

        JsonObject response = getResponse(login);

        Json.createWriter(s.getOutputStream()).writeObject(response);
        s.close();
    }

    JsonObject getResponse(Login login) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        if (login == null) {
            logger.Log(LogType.Warning, "User not found");

            return factory.createObjectBuilder()
                    .add("success", false)
                    .build();
        } else {
            logger.Log(LogType.Verbose, "User found", login);

            Key key = keyManager.getRandomKey();
            logger.Log(LogType.Verbose, "Generated key", key);

            return factory.createObjectBuilder()
                    .add("success", true)
                    .add("key", toHexString(cipher.decrypt(key.key)))
                    .build();
        }

    }
}
