package se.kth.iv1350.pos.view;

/**
 * Handles and prints the messages from exceptions.
 */

public class ExceptionMessageHandler {

    /**
     * Builds the exception message string and prints it to <code>System.out</code>.
     * @param msg The specified message in the exception to include in the built exception message to <code>System.out</code>.
     */

    void printExceptionMessage(String msg){
        StringBuilder builder = new StringBuilder();
        builder.append("-- ERROR --");
        builder.append("\nMessage: ").append(msg);
        System.out.println(builder);
    }
}
