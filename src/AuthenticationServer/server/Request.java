package AuthenticationServer.server;

import AuthenticationServer.Login;

import java.util.Date;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Request {
    final Login login;
    final Date expiryDate;
    final String serviceName;

    protected Request(Login login, Date time, String serviceName) {
        this.login = login;
        this.expiryDate = time;
        this.serviceName = serviceName;
    }
}
