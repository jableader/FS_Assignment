package AuthenticationServer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginManager {
    protected Map<String, Login> users = new HashMap<>();

    public LoginManager(File source) throws IOException{
        Scanner sc = new Scanner(new FileInputStream(source));

        while (sc.hasNextLine()){
            String[] details = sc.nextLine().split(" ");
            addLogin(new Login(details[0], details[1]));
        }
    }

    protected void addLogin(Login login){
        users.put(login.id, login);
    }

    public Login getLogin(String username) {
        return users.get(username);
    }
}
