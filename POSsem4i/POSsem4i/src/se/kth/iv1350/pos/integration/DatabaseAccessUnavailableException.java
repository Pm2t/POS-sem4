package se.kth.iv1350.pos.integration;

/**
 * Thrown when a database could not be reached.
 */
public class DatabaseAccessUnavailableException extends RuntimeException{

    /**
     * Passes the exception message to the super class
     * @param msg The message with a description of the exception condition being sent to the super class.
     */
    public DatabaseAccessUnavailableException(String msg){
        super(msg);
    }
}
