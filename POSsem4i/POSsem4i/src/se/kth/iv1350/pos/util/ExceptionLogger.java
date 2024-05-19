package se.kth.iv1350.pos.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logs exception information to a text file.
 */

public class ExceptionLogger {
    private final PrintWriter printWriter;

    /**
     * Creates a new <code>PrintWriter</code> with a new <code>FileWriter</code> and assigns a new txt file name.
     * Uses auto flush.
     * Catches <code>IOException</code> if it could not log.
     * @throws IOException if the named file for any reason can't be opened.
     */

    public ExceptionLogger() throws IOException {
        FileWriter fileToLogTo = new FileWriter("ExceptionLog.txt");
        printWriter = new PrintWriter(fileToLogTo, true);
    }

    /**
     * Logs the message of the exception and the stack trace to the <code>PrintWriter printWriter</code> .
     * @param exception The exception that was thrown.
     */

    public void log(Exception exception){
        StringBuilder builder = new StringBuilder();
        builder.append("-- ERROR --");
        builder.append("\n Message: ");
        builder.append(exception.getMessage());
        builder.append("\n\nStack trace: \n");
        printWriter.println(builder);

        exception.printStackTrace(printWriter);
    }
}
