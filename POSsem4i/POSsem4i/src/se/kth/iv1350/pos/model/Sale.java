package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.integration.ReceiptDTO;
import se.kth.iv1350.pos.integration.SaleDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>Sale</code> handles the sale information and updates it based on what the customer wants to buy.
 */
public class Sale {
    private String timeOfSale;
    private final List<ItemWithQuantity> soldItems;
    private double totalPrice;
    private List<ItemOnReceipt> itemsOnReceipt;
    private final List<SaleObserver> saleObservers = new ArrayList<>();
    private SaleDTO saleDTO;

    /**
     * This constructor initializes the <code>timeOfSale</code> variable and the <code>ArrayList soldItems</code>.
     */
    public Sale(){
        setTimeOfSale();
        soldItems = new ArrayList<>();
        totalPrice = 0;
    }

    private void setTimeOfSale(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        timeOfSale = currentDateTime.format(formatter);
    }

    /**
     * <code>updateSale</code> updates the sale with the item being sold, the item with the provided <code>itemDTO</code>,
     * if the item is valid and does not already exist in the sale.
     * @param itemInfo The DTO of the item being sold.
     */
    public void updateSale(ItemDTO itemInfo){

        if(itemIDIsValid(itemInfo)) {
            if (!itemDoesAlreadyExist(itemInfo))
                addNewItem(itemInfo);
            increaseTotalPrice(itemInfo);
        }
    }

    private boolean itemDoesAlreadyExist(ItemDTO itemInfo){
        int idOfItemBeingSold = itemInfo.getItemID();

        for (ItemWithQuantity soldItem : soldItems) {
            if (idOfItemBeingSold == soldItem.getItemID()) {
                soldItem.increaseQuantity();
                return true;
            }
        }
        return false;
    }

    private void addNewItem(ItemDTO itemInfo){
        soldItems.add(new ItemWithQuantity(itemInfo));
    }

    private boolean itemIDIsValid(ItemDTO itemInfo){
        return !itemInfo.getItemName().equals("Unknown item");
    }

    private void increaseTotalPrice(ItemDTO itemInfo){
        totalPrice += calculateItemPriceInclVAT(itemInfo);

    }

    private double calculateItemPriceInclVAT(ItemDTO itemInfo){
        return itemInfo.getItemPrice()*(1 + itemInfo.getItemVAT());
    }

    /**
     * Getter method for the total cost of the sale.
     * @return Returns the total cost.
     */
    public double getTotalPrice(){
        return totalPrice;
    }

    private double calculateChange(double paidAmount){
        return paidAmount - totalPrice;
    }

    /**
     * <code>startReceipt</code> initializes a new receipt. It creates a new list with the items going on the receipt, calculates
     * the change, creates a new <code>Receipt</code> object and sends it to the printer.
     * @param printer The printer the receipt will be printed on.
     * @param paidAmount The amount paid, provided by the cashier.
     */
    public double startReceipt(Printer printer, double paidAmount){
        createItemOnReceiptList();

        double change = calculateChange(paidAmount);

        ReceiptDTO receiptDTO = new ReceiptDTO(itemsOnReceipt, totalPrice, paidAmount, timeOfSale, change);
        Receipt receipt = new Receipt(receiptDTO);
        printer.printReceipt(receipt);

        updateObservers();

        return change;
    }

    private void createItemOnReceiptList(){

        itemsOnReceipt = new ArrayList<>();

        for (ItemWithQuantity soldItem : soldItems) {
            itemsOnReceipt.add(new ItemOnReceipt(soldItem));
        }
    }

    /**
     * Getter method for the sale information. When called, it creates a new <code>SaleDTO</code> containing the items being sold.
     * @return Returns the newly made <code>SaleDTO</code> object.
     */
    public SaleDTO getSaleInfo(){
        this.saleDTO = new SaleDTO(soldItems, totalPrice);
        return saleDTO;
    }

    private void updateObservers() {
        for (SaleObserver saleObserver : saleObservers) {
            saleObserver.saleCompleted(saleDTO);
        }
    }

    /**
     * Adds observers.
     * @param saleObserver The observer that will be added.
     */
    public void addSaleObserver(SaleObserver saleObserver) {
        saleObservers.add(saleObserver);
    }
}


