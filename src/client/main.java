package client;

import common.RType;
import security.BasicCipher;
import security.Cipher;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

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
                        .add("serviceName", RType.GetTicketGrantingTicket.id)
                        .build());

        JsonObject response = Json
                .createReader(socket.getInputStream())
                .readObject();

        System.out.println(response.toString());
        System.out.println(new String(fromHexString(response.getString("tgt"))));

        Cipher cipher = new BasicCipher("password123".getBytes());
        System.out.println(decode(response.getString("sessionKey"), cipher));
    }

    static String decode(String hexString, Cipher cipher) throws IOException {
        byte[] bytes = fromHexString(hexString);
        InputStream decipheringStream = cipher.getDecipheringStream(new ByteArrayInputStream(bytes));

        byte[] deciphered = new byte[bytes.length];
        decipheringStream.read(deciphered);
        return new String(deciphered);
    }
}