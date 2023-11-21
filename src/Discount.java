/**
 * @author Erik Hillborg, erik.hillborg@iths.se
 */

public class Discount {
    private double percent;

    public Discount(double percent) {
        if (percent > 0 && percent < 100) {
            this.percent = percent;
        } else {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }

    public Discount (Discount other) {
        this(other.getPercent());
    }

    public double getPercent() {
        return percent;
    }

    public Price apply(Price priceToReduce) {
        double price = priceToReduce.getValue();

        return new Price(price - (price * (this.percent / 100)), false);
    }

    @Override
    public String toString() {
        return "" + this.percent;
    }
}
