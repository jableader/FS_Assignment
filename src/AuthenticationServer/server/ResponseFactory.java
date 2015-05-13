package AuthenticationServer.server;

import AuthenticationServer.LoginManager;
import Security.Cipher;

/**
 * Created by Jableader on 12/05/2015.
 */
public class ResponseFactory {
    final Cipher tgsCipher;
    final Cipher sessionCipher;
    final LoginManager loginManager;

    public ResponseFactory(Cipher tgsCipher, Cipher sessionCipher, LoginManager loginManager) {
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
        this.loginManager = loginManager;
    }

    public Response getResponse(Request rq) {
        return new Response(
                rq.login,

        )
    }
}
