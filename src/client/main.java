package client;

import common.Services;
import security.BasicCipher;
import security.Cipher;
import security.EmptyCipher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static common.Tools.fromHexString;
import static common.Tools.millisFromNow;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class main {
    final static String authAddress = "localhost";
    final static int authPort = 8888;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(authAddress, authPort);

        JsonObjectBuilder jb = Json.createObjectBuilder();
        Json.createWriter(socket.getOutputStream())
                .writeObject(jb
                        .add("id", "bob")
                        .add("expiry", millisFromNow(1000 * 60 * 60).getTime())
                        .add("serviceName", Services.GetTicketGrantingTicket.id)
                        .build());

        JsonObject response = Json
                .createReader(socket.getInputStream())
                .readObject();

        System.out.println(response.toString());
        System.out.println(decode(response.getString("tgs"), new EmptyCipher()));
        System.out.println(decode(response.getString("sessionKey"), new BasicCipher("password123".getBytes())));
    }

    static String decode(String hexString, Cipher cipher) throws IOException {
        byte[] bytes = fromHexString(hexString);
        InputStream decipheringStream = cipher.getDecipheringStream(new ByteArrayInputStream(bytes));

        byte[] deciphered = new byte[bytes.length];
        decipheringStream.read(deciphered);
        return new String(deciphered);
    }
}