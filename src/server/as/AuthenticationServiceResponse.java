package server.as;

import common.Key;
import common.KeyManager;
import common.Login;
import common.Tools;
import logging.Logger;
import security.Cipher;
import security.implementations.XorWithKey;
import server.Response;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.toBase64;

/**
 * Created by Jableader on 14/5/2015.
 */
class AuthenticationServiceResponse extends Response {
    private final Key key;
    private final Login login;
    private final InetAddress clientAddress;
    private final Cipher tgsSecretCipher;
    private final Cipher clientSecretCipher;

    public AuthenticationServiceResponse(Logger logger, Login login, InetAddress address, Cipher tgsCipher, KeyManager keyManager, Date dateCreated, Date expiry) {
        super(logger, dateCreated);
        this.login = login;
        this.clientAddress = address;
        this.tgsSecretCipher = tgsCipher;
        this.clientSecretCipher = Tools.cipherForUseBetweenClientAndServer(login.password, logger);

        if (wasSuccess()) {
            key = keyManager.generateKey(expiry);
        } else {
            key = null;
        }
    }

    @Override
    public boolean wasSuccess() {
        return login != null;
    }

    @Override
    protected JsonObjectBuilder getJsonResponse() {
        JsonObjectBuilder base = super.getJsonResponse();

        if (wasSuccess()) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            Json.createGenerator(clientSecretCipher.getCipheringStream(byteStream))
                    .writeStartObject()
                    .write("time", timeCreated.getTime())
                    .write("expiry", key.expiry.getTime())
                    .write("key", toBase64(key.key))
                    .writeEnd()
                    .close();

            base.add("sessionKey", toBase64(byteStream.toByteArray()));

            byteStream = new ByteArrayOutputStream();
            Json.createGenerator(tgsSecretCipher.getCipheringStream(byteStream))
                    .writeStartObject()
                    .write("id", login.id)
                    .write("clientAddress", clientAddress.toString())
                    .write("time", timeCreated.getTime())
                    .write("expiry", key.expiry.getTime())
                    .write("key", toBase64(key.key))
                    .writeEnd()
                    .close();

            base.add("tgt", toBase64(byteStream.toByteArray()));
        }

        return base;
    }
}
