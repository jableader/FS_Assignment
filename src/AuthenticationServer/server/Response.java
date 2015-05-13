package AuthenticationServer.server;

import AuthenticationServer.Key;
import AuthenticationServer.Login;
import Logging.Logger;
import Security.Cipher;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

import static Common.Tools.*;

/**
 * Created by Jableader on 12/05/2015.
 */
public class Response {
    final Login login;
    final Key key;
    final Date timeCreated;
    final Cipher tgsCipher;
    final Cipher sessionCipher;

    protected Response(Login login, Key key, Date timeCreated, Cipher tgsCipher, Cipher sessionCipher) {
        this.login = login;
        this.key = key;
        this.timeCreated = timeCreated;
        this.tgsCipher = tgsCipher;
        this.sessionCipher = sessionCipher;
    }

    public boolean wasSuccess() {
        return login != null;
    }

    /**
     * Writes response to stream s.
     *
     * Encrypts sensitive parameters, including
     * expireyDate, session key and timestamp
     */
    public void writeResponse(OutputStream s) {
        JsonGenerator responseWriter = Json.createGenerator(s)
                .write("success", wasSuccess());

        if (wasSuccess()) {
            JsonBuilderFactory factory = Json.createBuilderFactory(null);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            Json.createGenerator(tgsCipher.getCipheringStream(byteStream))
                    .write("id", login.id)
                    .write("time", timeCreated.getTime())
                    .write("expirey", key.expirey.getTime())
                    .write("key", toHexString(key.key));

            responseWriter.write("tgt", toHexString(byteStream.toByteArray()));

            byteStream = new ByteArrayOutputStream();
            Json.createGenerator(sessionCipher.getCipheringStream(byteStream))
                    .write("time", timeCreated.getTime())
                    .write("expirey", key.expirey.getTime())
                    .write("key", toHexString(key.key));

            responseWriter.write("sessionKey", toHexString(byteStream.toByteArray()));
        }
    }
}
