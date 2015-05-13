package Server.internals;

import Server.management.Login;

import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Request {
    public final Login login;
    public final Date expiryDate;
    public final Server.internals.RType type;

    public RType getType() {
        return type;
    }

    protected Request(Login login, Date expiry, RType type) {
        this.login = login;
        this.expiryDate = expiry;
        this.type = type;
    }
}
