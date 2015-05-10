package AuthenticationServer;

import Logging.LogType;
import Logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jableader on 10/5/2015.
 */
public class Server {
    final Logger logger;
    final LoginManager logins;

    public Server(Logger logger, LoginManager manager) {
        this.logins = manager;
        this.logger = logger;

        logger.Log(LogType.Verbose, "Logger initialised");
    }

    ServerSocket serverSocket;
    public void Serve() throws IOException{
        logger.Log(LogType.Standard, "Starting Server");
        serverSocket = new ServerSocket(8888);
    }

    public void ServeSocket(Socket s) throws IOException{
        logger.Log(LogType.Standard, "Connected to " + s.getInetAddress().getHostAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String username = in.readLine();

        logger.Log(LogType.Standard, "Looking up user " + username);

        Login user = logins.getLogin(username);

        if (user != null) {
            
        }
        s.close();
    }
}
