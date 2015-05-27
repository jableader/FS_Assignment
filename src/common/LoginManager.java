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

    public LoginManager(Logger logger, File source) throws IOException {
        this.logger = logger;

        logger.Log(LogType.Standard, "Loading logins from file");
        Scanner sc = new Scanner(new FileInputStream(source));

        while (sc.hasNextLine()) {
            String[] details = sc.nextLine().split(" ");
            addLogin(new Login(details[0], fromBase64(details[1])));
        }

        logger.Log(LogType.Standard, "Loaded " + users.size() + " users");
    }

    protected void addLogin(Login login) {
        logger.Log(LogType.Standard, "Adding user " + login.id);

        users.put(login.id, login);
    }

    public Login getLogin(String id) {
        Login l = users.get(id);
        logger.Log(LogType.Verbose, "Looking for user " + id + "; Found: " + (l != null));

        return l;
    }
}
