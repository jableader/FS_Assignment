package logging;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public enum LogType {
    Error,
    Warning,
    Verbose,
    Standard,
    Cipher;

    public boolean IsError() {
        return this == Error || this == Warning;
    }
}
