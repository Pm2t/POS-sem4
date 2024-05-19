package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.ItemWithQuantity;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for the sold items. Used to pass to other layers.
 */
public class SaleDTO {
    private final List<ItemWithQuantity> soldItems;
    private final double totalPrice;

    /**
     * The constructor copies the list of items being sold <code>soldItems</code>
     * from <code>Sale</code> into its own list.
     * @param soldItems The list of items being sold.
     */
    public SaleDTO(List<ItemWithQuantity> soldItems, double totalPrice){
        this.soldItems = new ArrayList<>();
        this.totalPrice = totalPrice;

        for(int i = 0; i < soldItems.size(); i++){
            this.soldItems.add(i, soldItems.get(i));
        }
    }

    /**
     * Getter method for the list of sold items belonging to this class.
     * @return Returns the list of sold items.
     */
    public List<ItemWithQuantity> getSoldItems(){
        return soldItems;
    }

    /**
     * Getter method for the total price of the sale.
     * @return Returns the total price.
     */
    public double getTotalPrice() {
        return  totalPrice;
    }
}
