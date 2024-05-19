package se.kth.iv1350.pos.controller;

import se.kth.iv1350.pos.integration.*;
import se.kth.iv1350.pos.model.Register;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleObserver;
import se.kth.iv1350.pos.util.ExceptionLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <code>Controller</code> handles method calls from <code>View</code> to packages <code>model</code>
 * and <code>integration</code>.
 */
public class Controller {
    private final Printer printer;
    private final ExtAccSys extAccSys;
    private final ExtInvSys extInvSys;
    private final Register register;
    private Sale currentSale;
    private final ExceptionLogger exceptionLogger;
    private final List<SaleObserver> saleObservers = new ArrayList<>();

    /**
     * This constructor initializes the <code>controller</code>> with specific objects of the external accounting and
     * inventory systems. It also initializes the controller with an object of <code>Printer</code>, <code>Register</code>
     * and the <code>ExceptionLogger</code>.
     * @param creator This is an object of the class <code>CreateExternalSys</code> which is used to get
     *                objects of the external systems.
     * @param printer This is the object of the <code>Printer</code> class.
     * @throws IOException if there is an error with the file output.
     */
    public Controller(CreateExternalSys creator, Printer printer) throws IOException {
        extAccSys = creator.getExtAccSys();
        extInvSys = creator.getExtInvSys();
        this.printer = printer;
        register = new Register();
        exceptionLogger = new ExceptionLogger();
    }

    /**
     * Initializes the sale and creates a new <code>Sale</code> object and registers the observers to it.
     */
    public void startNewSale(){
        currentSale = new Sale();
        for (SaleObserver saleObserver : saleObservers) {
            currentSale.addSaleObserver(saleObserver);
        }
    }

    /**
     * <code>enterItem</code> gets an item DTO from <code>ExtInvSys</code> and passes it to <code>updateSale</code>
     * in <code>Sale</code>. It also returns the item DTO <code>itemInfo</code> with information about the item
     * to <code>View</code> so it can be displayed.
     * @param itemID The ID of the item, provided by the cashier.
     * @return Returns the item DTO.
     * @throws IdentifierDoesNotExistException if the provided item id is not in the inventory system.
     * @throws SystemOperationFailedException if unable to enter item due to other reasons than a wrong item id.
     */
    public ItemDTO enterItem(int itemID) throws IdentifierDoesNotExistException, SystemOperationFailedException {
        try{
            ItemDTO itemInfo = extInvSys.getItemInfo(itemID);
            currentSale.updateSale(itemInfo);
            return itemInfo;
        }catch (DatabaseAccessUnavailableException daue){
            exceptionLogger.log(daue);
            throw new SystemOperationFailedException("Item could not be entered.", daue);
        }
    }

    /**
     * Getter method for the total cost of the sale.
     * @return Returns the total cost of the sale.
     */
    public double getTotalPrice(){
        return currentSale.getTotalPrice();
    }

    /**
     * <code>endSale</code> is called when the sale is ended. It provides information needed after the sale. For now, it is
     * the total cost of the sale.
     * @return Returns total cost of sale.
     */
    public double endSale(){
        return currentSale.getTotalPrice();
    }

    /**
     * <code>payment</code> handles the actions needed when the customer pays. That includes updating the external systems
     * with the relevant information, updating the amount in the register and print a receipt.
     * @param paidAmount The paid amount, provided by the cashier.
     * @return Returns the change amount to give the customer.
     */
    public double payment(double paidAmount){
        sendSaleInfoToExternalSystems();

        register.enterAmountPaid(paidAmount);

        double amountGivenInChange = currentSale.startReceipt(printer, paidAmount);

        register.subtractChangeAmount(amountGivenInChange);

        return amountGivenInChange;
    }

    private void sendSaleInfoToExternalSystems(){
        extAccSys.sendSaleInformation(currentSale.getSaleInfo());
        extInvSys.sendSaleInformation(currentSale.getSaleInfo());
    }

    /**
     * notifies the observer that the sale has ended.
     * @param saleObserver The observer to be notified.
     */
    public void addSaleObservers(SaleObserver saleObserver) {
        saleObservers.add(saleObserver);
    }
}
