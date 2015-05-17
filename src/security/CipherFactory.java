package security;

import server.Request;

import javax.crypto.*;
import javax.crypto.Cipher;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public interface CipherFactory {
    Cipher getCipher(Request r);
}
