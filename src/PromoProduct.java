import java.util.List;

public class PromoProduct extends Product {

    private double reducedPrice;

    public PromoProduct(String name, double reducedPrice) {
        super(name);
        this.reducedPrice = reducedPrice;
    }

    public PromoProduct(String name, Price price, double reducedPrice) {
        super(name, price);
        this.reducedPrice = reducedPrice;
    }

    public PromoProduct(String name, List<String> productGroups, double reducedPrice) {
        super(name, productGroups);
        this.reducedPrice = reducedPrice;
    }

    public PromoProduct(String name, Price price, List<String> productGroups, double reducedPrice) {
        super(name, price, productGroups);
        this.reducedPrice = reducedPrice;
    }
}
