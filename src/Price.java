public class Price {
    private double value;
    private boolean isPerKG;

    public Price(double value, boolean isPerKG) {
        if (value > 0) {
            this.value = value;
        }
        this.isPerKG = isPerKG;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value > 0) {
            this.value = value;
        }
    }

    public boolean isPerKG() {
        return isPerKG;
    }

    public void setPerKG(boolean perKG) {
        isPerKG = perKG;
    }
}
