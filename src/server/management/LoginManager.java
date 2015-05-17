package server.management;

import logging.LogType;
import logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static common.Tools.fromHexString;

public class LoginManager {
    protected Map<String, Login> users = new HashMap<>();
    protected final Logger logger;

    public LoginManager(Logger logger, Iterable<Login> logins) {
        this.logger = logger;

        logger.Log(LogType.Standard, "Loaded " + users.size() + " users");

        for (Login l: logins)
            addLogin(l);
    }

    public LoginManager(Logger logger, File source) throws IOException{
        this.logger = logger;

        logger.Log(LogType.Standard, "Loading logins from file");
        Scanner sc = new Scanner(new FileInputStream(source));

        while (sc.hasNextLine()){
            String[] details = sc.nextLine().split(" ");
            addLogin(new Login(details[0], fromHexString(details[1])));
        }
    }

    protected void addLogin(Login login){
        logger.Log(LogType.Standard, "Adding user " + login.id);

        users.put(login.id, login);
    }

    public Login getLogin(String id) {
        Login l = users.get(id);
        logger.Log(LogType.Verbose, "Looking for user " + id + "; Found: " + (l != null));

        return l;
    }
}
