/**
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.List;

public class PromoProduct extends Product {
    private Discount discount;
    private int prerequisite;

    public PromoProduct(String name, Price price, List<String> productGroups, Discount discount, int prerequisite) {
        super(name, price, productGroups);
        this.discount = discount;
        this.prerequisite = prerequisite;
    }

    public Discount getDiscount() {
        return discount;
    }

    public int getPrerequisite() {
        return prerequisite;
    }

    /**
     * @return A string representation of the reduced price.
     */
    public String displayReducedPrice() {
        double discount = this.price.getValue() * this.discount.getPercent();
        double priceWithDiscount = this.price.getValue() - discount;
        return "" + priceWithDiscount;
    }
}
