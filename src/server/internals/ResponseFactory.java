package server.internals;

import server.management.Key;
import server.management.KeyManager;
import server.management.Login;
import server.management.LoginManager;
import logging.Logger;
import security.Cipher;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import static common.Tools.toHexString;

/**
 * Created by Jableader on 12/05/2015.
 */
public class ResponseFactory {
    private final Cipher tgsCipher;
    private final Cipher sessionCipher;
    private final LoginManager loginManager;
    private final KeyManager keyManager;
    private final Logger logger;

    public ResponseFactory(Cipher tgsCipher, Cipher sessionCipher, LoginManager loginManager, KeyManager keyManager, Logger logger) {
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
        this.loginManager = loginManager;
        this.keyManager = keyManager;
        this.logger = logger;
    }

    public Response getResponse(Request rq) {
        switch (rq.getType()) {
            case GetTgsKey:
                return new TgsResponse(logger, rq.login, new Date(), rq.expiryDate);
            default:
                return new Response(logger, rq.login, new Date());
        }
    }

    private class TgsResponse extends Response {
        private final Key key;

        public TgsResponse(Logger logger, Login login, Date dateCreated, Date expiry) {
            super(logger, login, dateCreated);

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
