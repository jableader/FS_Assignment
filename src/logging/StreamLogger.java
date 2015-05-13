package logging;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jableader on 10/5/2015.
 */
public class StreamLogger implements Logger {
    final DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
    final PrintWriter stdOut, stdError;
    final boolean verbose;

    public StreamLogger(PrintWriter stdOut, PrintWriter stdError, boolean verbose) {
        this.stdOut = stdOut;
        this.stdError = stdError;
        this.verbose = verbose;
    }

    public void Log(LogType type, String message, Object... args){
        if (verbose || type != LogType.Verbose) {
            PrintWriter output = (type.IsError()) ? stdError : stdOut;

            output.printf("[%s] %s: %s\n", formatter.format(new Date()), type, message);
        }
    }

}