
package se.kth.iv1350.pos.view;


import se.kth.iv1350.pos.integration.SaleDTO;
import se.kth.iv1350.pos.model.SaleObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logs the sum of all costs (including VAT) for all sales since the program started.
 */
public class TotalRevenueFileOutput implements SaleObserver {
    private double totalRevenue;
    private final PrintWriter printWriter;

    /**
     * Creates a new <code>PrintWriter</code> with a new <code>FileWriter</code> and assigns a new txt file name.
     * Uses auto flush.
     * Catches <code>IOException</code> if it could not log.
     * @throws IOException if the named file for any reason can't be opened.
     */
    public TotalRevenueFileOutput() throws IOException {
        FileWriter fileToLogTotalRevenue = new FileWriter("TotalRevenue.txt");
        printWriter = new PrintWriter(fileToLogTotalRevenue, true);
    }

    /**
     * Logs the total revenue to <code>System.out</code>.
     * @param saleTotalPrice The total price of items in the sale.
     */
    @Override
    public void saleCompleted(SaleDTO saleTotalPrice) {
        totalRevenue += saleTotalPrice.getTotalPrice();
        logTotalRevenue();
    }


    private void logTotalRevenue() {
        StringBuilder builder = new StringBuilder();
        builder.append("Total revenue: ");
        builder.append(totalRevenue);
        builder.append(" SEK");
        printWriter.println(builder);
    }

}
