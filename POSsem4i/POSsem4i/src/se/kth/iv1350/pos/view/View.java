package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.controller.SystemOperationFailedException;
import se.kth.iv1350.pos.integration.IdentifierDoesNotExistException;
import se.kth.iv1350.pos.integration.ItemDTO;

import java.io.IOException;
import java.util.Locale;

/**
 * <code>View</code> handles interactions with the user. This class is a placeholder for the user interface.
 */
public class View {
    private final Controller controller;

    /**
     * This constructor initializes the <code>view</code> with an object of <code>Controller</code>.
     * @param controller This is the object this class initializes with.
     * @throws IOException if there is an error with the file output.
     */
    public View(Controller controller) throws IOException {
        this.controller = controller;
        controller.addSaleObservers(new TotalRevenueView());
        controller.addSaleObservers(new TotalRevenueFileOutput());
    }

    /**
     * <code>simulateApplication</code> calls the methods in controller to simulate a run of this application
     * as it is intended in real use.
     */
    public void simulateApplication(){
        ExceptionMessageHandler exceptionMessageHandler = new ExceptionMessageHandler();
        controller.startNewSale();
        System.out.println("\nA new sale has been started");

        double totalPrice;
        try {
            ItemDTO itemInfo = controller.enterItem(11);
            totalPrice = controller.getTotalPrice();
            itemDisplay(itemInfo, totalPrice);

            itemInfo = controller.enterItem(12);
            totalPrice = controller.getTotalPrice();
            itemDisplay(itemInfo, totalPrice);

            totalPrice = controller.endSale();
            endSaleDisplay(totalPrice);

            double amountPaid = 100;
            amountPaidDisplay(amountPaid);

            double amountChange = controller.payment(amountPaid);
            System.out.println("\nChange to give the customer: " + amountChange + " SEK");
        }catch (IdentifierDoesNotExistException idnee){
            exceptionMessageHandler.printExceptionMessage("Item with ID: " + idnee.getItemIDThatDoesNotExist()
                    + " not found.");
        }catch (SystemOperationFailedException sofe){
            exceptionMessageHandler.printExceptionMessage("Item could not be entered, please try again.");
        }


    }


    private void itemDisplay(ItemDTO itemInfo, double totalPrice){

        System.out.println("\nAdd 1 item with item id: " + itemInfo.getItemID());
        System.out.println("Item ID: " + itemInfo.getItemID());
        System.out.println("Item name: " + itemInfo.getItemName());
        System.out.printf(Locale.US,"Item price: %.2f", itemInfo.getItemPrice());
        System.out.println(" SEK");
        System.out.printf(Locale.US,"Item VAT: %.0f", itemInfo.getItemVAT()*100);
        System.out.println("%");
        System.out.println("Item description: " + itemInfo.getItemDescription());

        System.out.printf(Locale.US, "Total cost (incl VAT): %.2f", totalPrice);
        System.out.println(" SEK");
    }

    private void endSaleDisplay(double totalPrice) {
        System.out.println("\nEnd sale: ");
        System.out.printf(Locale.US,"Total cost (incl VAT): %.2f", totalPrice);
        System.out.println(" SEK");
    }

    private void amountPaidDisplay(double amountPaid) {
        System.out.println("\nCustomer pays: " + amountPaid + " SEK.");

        System.out.println("\nSale information sent to accounting and inventory system.");
        System.out.println("Inventory system updates inventory quantity of the items sold.");
        System.out.println("Balance in register is updated.");
    }
}
