package AuthenticationServer;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginManager {
    protected Map<String, Login> users;

    public LoginManager(File source) {

    }

    public Login getLogin(String username) {
        return users.get(username);
    }
}
