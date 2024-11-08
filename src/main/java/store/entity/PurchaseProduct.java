package store.entity;

public class PurchaseProduct {

    private final String productName;
    private final int quantity;

    public PurchaseProduct(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
