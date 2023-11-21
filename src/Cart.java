/**
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> items;
    private static int noOfItems = 0;
    private double totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalPrice = 0;
    }

    public List<Product> getItems() {
        return items;
    }

    public int noOfDuplicates(Product product) {
        int counter = 0;
        for (Product item : this.items) {
            if (product.equals(item)) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isDiscountable(PromoProduct product, double amount) {
        return noOfDuplicates(product) + amount >= product.getPrerequisite();
    }

    public void applyDiscount(PromoProduct product, double amount) {
        if (isDiscountable(product, amount)) {
            Price reduced = product.getDiscount().apply(product.price);
            product.price.setValue(reduced.getValue());

            if (!product.price.isPerKG()) {
                // Setting all items prices in cart too discounted price.
                for (Product item : this.items) {
                    if (item.equals(product)) {
                        item.price.setValue(reduced.getValue());
                    }
                }
            }
        }
    }

    public void add(Product product) {
        this.items.add(product);
        this.totalPrice += product.getPrice().getValue();
        noOfItems++;
    }

    public void add(List<Product> products) {
        for (Product product : products) {
            this.add(product);
        }
    }

    @Override
    public String toString() {
        String strOut;
        StringBuilder stringBuilder = new StringBuilder();
        for (Product item : this.items) {
            stringBuilder
                    .append(item.getName())
                    .append("\t\t")
                    .append(String.format("%5.2f", item.getPrice().getValue()))
                    .append(" kr")
                    .append("\n");
        }

        stringBuilder
                .append(noOfItems)
                .append(" items\t\tTotal price: ")
                .append(String.format("%5.2f", this.totalPrice));

        strOut = String.valueOf(stringBuilder);

        return strOut;
    }
}
