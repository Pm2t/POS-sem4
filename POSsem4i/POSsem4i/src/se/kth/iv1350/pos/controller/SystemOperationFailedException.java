package se.kth.iv1350.pos.controller;

/**
 * Thrown when an operation could not be performed in the system.
 */
public class SystemOperationFailedException extends Exception{

    /**
     * Initializes the message to the super class and includes the exception that caused the error.
     * @param msg The message with a description of the exception condition being sent to the super class.
     * @param exception The cause of the error.
     */
    public SystemOperationFailedException(String msg, Exception exception){
        super(msg, exception);
    }
}
