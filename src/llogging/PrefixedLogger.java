package logging;

import sun.rmi.runtime.Log;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class PrefixedLogger implements Logger{
    final Logger inner;
    final String prefix;

    public PrefixedLogger(Logger inner, String prefix) {
        this.inner = inner;
        this.prefix = prefix;
    }

    @Override
    public void Log(LogType type, String message, Object... stuff) {
        inner.Log(type, prefix + message, stuff);
    }
}
