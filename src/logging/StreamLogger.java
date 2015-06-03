package logging;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public class StreamLogger implements Logger {
    final DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
    final PrintStream stdOut, stdError;
    final boolean verbose;
    final boolean cipherSteps;

    public StreamLogger(PrintStream stdOut, PrintStream stdError, boolean verbose, boolean cipherSteps) {
        this.stdOut = stdOut;
        this.stdError = stdError;
        this.verbose = verbose;
        this.cipherSteps = cipherSteps;
    }

    public void Log(LogType type, String message, Object... args) {
        if ((verbose || type != LogType.Verbose) && (cipherSteps || type != LogType.Cipher)) {
            PrintStream output = (type.IsError()) ? stdError : stdOut;

            output.printf("[%s] %s\n", formatter.format(new Date()), message);
        }
    }

}
