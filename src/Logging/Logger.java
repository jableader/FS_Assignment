package Logging;

/**
 * Created by Jableader on 10/5/2015.
 */
public interface Logger {
    void Log(LogType type, String message, Object... stuff);
}
