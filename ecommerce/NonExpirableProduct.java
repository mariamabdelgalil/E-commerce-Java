package ecommerce;

public class NonExpirableProduct extends Product implements Shippable {
    private boolean needsShipping;
    private double weight;

    public NonExpirableProduct(String name, double price, int quantity, boolean needsShipping, double weight) {
        super(name, price, quantity);
        this.needsShipping = needsShipping;
        this.weight = weight;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public boolean requiresShipping() {
        return needsShipping;
    }

    @Override
    public double getWeight() {
        return needsShipping ? weight : 0.0;
    }
}
