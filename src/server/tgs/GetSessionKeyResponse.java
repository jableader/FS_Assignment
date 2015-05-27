package server.tgs;

import common.Key;
import common.KeyManager;
import logging.Logger;
import security.Cipher;
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
public class GetSessionKeyResponse extends Response {
    private final KeyManager keyManager;
    private final Date expiry;
    private final Cipher serviceSecretCipher;
    private final Cipher tgsSessionCipher;
    private final String clientId;
    private final InetAddress sourceAddress;

    public GetSessionKeyResponse(Logger logger, Date timeCreated, KeyManager keyManager, InetAddress source, Date expiry, Cipher serviceSecretCipher, Cipher tgsSessionCipher, String clientId, InetAddress sourceAddress) {
        super(logger, timeCreated);
        this.keyManager = keyManager;
        this.expiry = expiry;
        this.serviceSecretCipher = serviceSecretCipher;
        this.tgsSessionCipher = tgsSessionCipher;
        this.clientId = clientId;
        this.sourceAddress = sourceAddress;
    }

    @Override
    public boolean wasSuccess() {
        return true;
    }

    @Override
    protected JsonObjectBuilder getJsonResponse() {
        JsonObjectBuilder base = super.getJsonResponse();

        if (wasSuccess()) {
            Key clientSessionKey = keyManager.getRandomKey(expiry);

            ByteArrayOutputStream clientToServerStream = new ByteArrayOutputStream();
            Json.createGenerator(serviceSecretCipher.getCipheringStream(clientToServerStream))
                    .writeStartObject()
                        .write("id", clientId)
                        .write("address", sourceAddress.toString())
                        .write("expiry", clientSessionKey.expiry.getTime())
                        .write("key", toBase64(keyManager.getRandomKey(expiry).key))
                    .writeEnd()
                    .flush();

            base.add("clientTicket", toBase64(clientToServerStream.toByteArray()));

            base.add("key", toBase64(tgsSessionCipher.encryptBytes(clientSessionKey.key)));
        }

        return base;
    }
}
