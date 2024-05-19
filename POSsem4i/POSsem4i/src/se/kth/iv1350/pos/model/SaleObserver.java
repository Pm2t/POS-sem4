package se.kth.iv1350.pos.model;


import se.kth.iv1350.pos.integration.SaleDTO;

/**
 * A listener interface that receives information about the total sum from a completed sale.
 */
public interface SaleObserver {

    /**
     * This method is called when a sale has ended and the total cost of all items in the sale
     * have been paid.
     * @param saleTotalPrice The total price of all items in the sale.
     */
    void saleCompleted(SaleDTO saleTotalPrice);
}
