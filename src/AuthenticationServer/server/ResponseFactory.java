package AuthenticationServer.server;

import AuthenticationServer.Key;
import AuthenticationServer.KeyManager;
import AuthenticationServer.Login;
import AuthenticationServer.LoginManager;
import Security.Cipher;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import static Common.Tools.toHexString;

/**
 * Created by Jableader on 12/05/2015.
 */
public class ResponseFactory {
    private final Cipher tgsCipher;
    private final Cipher sessionCipher;
    private final LoginManager loginManager;
    private final KeyManager keyManager;

    public ResponseFactory(Cipher tgsCipher, Cipher sessionCipher, LoginManager loginManager, KeyManager keyManager) {
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
        this.loginManager = loginManager;
        this.keyManager = keyManager;
    }

    public Response getResponse(Request rq) {
        switch (rq.getType()) {
            case GetTgsKey:
                return new TgsResponse(rq.login, new Date(), rq.expiryDate);
            default:
                return new Response(rq.login, new Date());
        }
    }

    private class TgsResponse extends Response {
        private final Key key;

        public TgsResponse(Login login, Date dateCreated, Date expiry) {
            super(login, dateCreated);

            if (wasSuccess()) {
                key = keyManager.getRandomKey(expiry);
                keyManager.registerKey(login, key);
            } else {
                key = null;
            }
        }

        @Override
        public RType getType() {
            return RType.GetTgsKey;
        }

        @Override
        protected JsonObjectBuilder getJsonResponse() {
            JsonObjectBuilder base = super.getJsonResponse();

            if (wasSuccess()) {
                JsonBuilderFactory factory = Json.createBuilderFactory(null);

                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

                Json.createGenerator(tgsCipher.getCipheringStream(byteStream))
                        .write("id", login.id)
                        .write("time", timeCreated.getTime())
                        .write("expiry", key.expiry.getTime())
                        .write("key", toHexString(key.key));

                base.add("tgt", toHexString(byteStream.toByteArray()));

                byteStream = new ByteArrayOutputStream();
                Json.createGenerator(sessionCipher.getCipheringStream(byteStream))
                        .write("time", timeCreated.getTime())
                        .write("expiry", key.expiry.getTime())
                        .write("key", toHexString(key.key));

                base.add("sessionKey", toHexString(byteStream.toByteArray()));
            }

            return base;
        }
    }
}
