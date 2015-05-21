package as;

import logging.Logger;
import security.BasicCipher;
import security.Cipher;
import security.EmptyCipher;
import server.Response;
import as.management.Key;
import as.management.KeyManager;
import as.management.Login;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Date;

import static common.Tools.toHexString;

/**
 * Created by Jableader on 14/5/2015.
 */
class TgtResponse extends Response {
    private final Key key;
    private final Login login;
    private final InetAddress clientAddress;

    public TgtResponse(Logger logger, Login login, InetAddress address, KeyManager keyManager, Date dateCreated, Date expiry) {
        super(logger, dateCreated);
        this.login = login;
        this.clientAddress = address;

        if (wasSuccess()) {
            key = keyManager.getRandomKey(expiry);
            keyManager.registerKey(login, key);
        } else {
            key = null;
        }
    }

    protected Cipher getTgsCipher() {
        return new EmptyCipher();
    }

    protected Cipher getClientSecretCipher() {
        return new BasicCipher(login.password);
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

            Json.createGenerator(getClientSecretCipher().getCipheringStream(byteStream))
                    .writeStartObject()
                        .write("time", timeCreated.getTime())
                        .write("expiry", key.expiry.getTime())
                        .write("key", toHexString(key.key))
                    .writeEnd()
                    .flush();

            base.add("sessionKey", toHexString(byteStream.toByteArray()));

            byteStream = new ByteArrayOutputStream();
            Json.createGenerator(getTgsCipher().getCipheringStream(byteStream))
                    .writeStartObject()
                        .write("id", login.id)
                        .write("clientAddress", clientAddress.getCanonicalHostName())
                        .write("time", timeCreated.getTime())
                        .write("expiry", key.expiry.getTime())
                        .write("key", toHexString(key.key))
                    .writeEnd()
                    .flush();

            base.add("tgs", toHexString(byteStream.toByteArray()));
        }

        return base;
    }
}
