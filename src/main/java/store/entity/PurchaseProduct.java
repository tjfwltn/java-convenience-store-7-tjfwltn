package store.entity;

import java.util.Objects;

public class PurchaseProduct {

    private final String productName;
    private final int quantity;
    private int price;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PurchaseProduct that = (PurchaseProduct) object;
        return Objects.equals(getProductName(), that.getProductName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductName(), getQuantity());
    }

    public void setPrice(Product promotionProduct) {
        this.price = promotionProduct.getPrice();
    }

    public int getPrice() {
        return price;
    }
}
