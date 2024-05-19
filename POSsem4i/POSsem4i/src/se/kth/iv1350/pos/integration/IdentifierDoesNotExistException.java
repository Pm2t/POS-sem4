package se.kth.iv1350.pos.integration;

/**
 * Thrown when an entered item identifier does not exist.
 */
public class IdentifierDoesNotExistException extends Exception{
    private final int itemIDThatDoesNotExist;

    /**
     * Passes the exception message to the super class and initializes the variable holding the id that could
     * not be entered.
     * @param itemID Included in the error message to specify the id that don't exist.
     */
    public IdentifierDoesNotExistException(int itemID){
        super("Item with id " + itemID + " does not exist.");
        itemIDThatDoesNotExist = itemID;
    }

    /**
     * @return The not existing item id being entered.
     */
    public int getItemIDThatDoesNotExist() {
        return itemIDThatDoesNotExist;
    }
}
