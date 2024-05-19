package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.integration.SaleDTO;
import se.kth.iv1350.pos.model.SaleObserver;

/**
 * Displays the sum of all costs (including VAT) for all sales since the program started.
 */
public class TotalRevenueView implements SaleObserver {
    private double totalRevenue;


    /**
     * Prints the total revenue to <code>System.out</code>.
     * @param saleTotalPrice The total price of items in the sale.
     */
    @Override
    public void saleCompleted(SaleDTO saleTotalPrice) {
        totalRevenue += saleTotalPrice.getTotalPrice();
        printTotalRevenue();
    }

    private void printTotalRevenue() {
        System.out.println("Total revenue: " + totalRevenue + " SEK");
    }
}
