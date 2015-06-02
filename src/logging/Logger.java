package logging;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public interface Logger {
    void Log(LogType type, String message, Object... stuff);
}
