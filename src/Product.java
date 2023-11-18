/**
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {
    private String name;
    protected Price price;
    private List<String> productGroups;

    public Product(String name, Price price, List<String> productGroups) {
        this.name = capitalize(name);
        this.price = price;
        if (productGroups == null) {
            this.productGroups = new ArrayList<>();
            this.productGroups.add("Unassigned");
        } else {
            this.productGroups = productGroups;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<String> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<String> productGroups) {
        this.productGroups = productGroups;
    }

    /**
     * Makes the first char in string parameter uppercase and the rest of the chars lowercase
     * @param string the string to capitalize
     * @return the capitalized string
     */
    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Product) {
            return Objects.equals(this.name, ((Product) other).name);
        }
        else {
            return false;
        }
    }

    /**
     * Overrides the toString method for appropriate string representation
     * @return a string with product info
     */
    @Override
    public String toString() {
        String strName = String.format("Name: %-15s", this.name);

        String strPrice = String.format("Price: %5.2f", this.price.getValue());
        if (this.price.isPerKG()) {
            strPrice += " kr/kg";
        } else {
            strPrice += " st";
        }
        strPrice = String.format("%-20s", strPrice);

        String strCategory;
        if (this.productGroups.isEmpty()) {
            strCategory = "Categories: Unassigned";
        } else {
            strCategory = String.format("Categories: %s", this.productGroups);
        }

        return strName + "\t\t" +  strPrice + "\t\t" +  strCategory;
    }
}
