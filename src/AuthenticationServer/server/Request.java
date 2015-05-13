package AuthenticationServer.server;

import AuthenticationServer.Login;

import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Request {
    public final Login login;
    public final Date expiryDate;
    public final RType type;

    public RType getType() {
        return type;
    }

    protected Request(Login login, Date expiry, RType type) {
        this.login = login;
        this.expiryDate = expiry;
        this.type = type;
    }
}
