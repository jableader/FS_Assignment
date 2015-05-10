package Logging;

/**
 * Created by Jableader on 10/5/2015.
 */
public enum LogType {
    Error,
    Warning,
    Verbose,
    Standard;

    public boolean IsError() {
        return this == Error || this == Warning;
    }
}
