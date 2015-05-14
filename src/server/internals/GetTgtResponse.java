package server.internals;

import logging.Logger;
import security.Cipher;
import server.management.Key;
import server.management.KeyManager;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import static common.Tools.toHexString;

/**
 * Created by Jableader on 14/5/2015.
 */
class GetTgtResponse extends Response {
    private final Key key;
    private final Cipher clientSecretCipher;
    private final Cipher tgsCipher;

    public GetTgtResponse(Logger logger, Request request, KeyManager keyManager, Date dateCreated, Date expiry, Cipher tgsCipher, Cipher sessionCipher) {
        super(logger, request, dateCreated, RType.GetTicketGrantingTicket);

        this.clientSecretCipher = tgsCipher;
        this.tgsCipher = sessionCipher;

        if (wasSuccess()) {
            key = keyManager.getRandomKey(expiry);
            keyManager.registerKey(request.login, key);
        } else {
            key = null;
        }
    }

    @Override
    protected JsonObjectBuilder getJsonResponse() {
        JsonObjectBuilder base = super.getJsonResponse();

        if (wasSuccess()) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            Json.createGenerator(clientSecretCipher.getCipheringStream(byteStream))
                    .write("time", timeCreated.getTime())
                    .write("expiry", key.expiry.getTime())
                    .write("key", toHexString(key.key));

            base.add("sessionKey", toHexString(byteStream.toByteArray()));

            byteStream = new ByteArrayOutputStream();
            Json.createGenerator(tgsCipher.getCipheringStream(byteStream))
                    .write("id", request.login.id)
                    .write("address", request.clientAddress.getCanonicalHostName())
                    .write("time", timeCreated.getTime())
                    .write("expiry", key.expiry.getTime())
                    .write("key", toHexString(key.key));

            base.add("tgt", toHexString(byteStream.toByteArray()));
        }

        return base;
    }
}
