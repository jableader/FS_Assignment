package common;

import logging.LogType;
import logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static common.Tools.fromBase64;

public class LoginManager {
    protected Map<String, Login> users = new HashMap<>();
    protected final Logger logger;

    public LoginManager(Logger logger, Iterable<Login> logins) {
        this.logger = logger;

        for (Login l : logins)
            addLogin(l);

        logger.Log(LogType.Standard, "Loaded " + users.size() + " users");
    }

    public LoginManager(Logger logger, File source)  {
        this.logger = logger;

        try {
            logger.Log(LogType.Standard, "Loading logins from file");
            Scanner sc = new Scanner(new FileInputStream(source));

            while (sc.hasNextLine()) {
                String[] details = sc.nextLine().split(" ");
                addLogin(new Login(details[0], fromBase64(details[1])));
            }
        } catch (IOException ex) {
            logger.Log(LogType.Warning, "Could not load from logins.txt");
        }

        if (users.size() == 0) {
            addLogin(new Login("bob", "password123".getBytes()));
        }

        logger.Log(LogType.Standard, "Loaded " + users.size() + " users");
    }

    protected void addLogin(Login login) {
        logger.Log(LogType.Verbose, "Adding user " + login.id);

        users.put(login.id, login);
    }

    public Login getLogin(String id) {
        Login l = users.get(id);
        logger.Log(LogType.Verbose, "Looking for user " + id + "; Found: " + (l != null));

        return l;
    }
}
