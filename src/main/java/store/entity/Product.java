package store.entity;


import store.domain.Promotion;

import java.text.DecimalFormat;
import java.util.Objects;

public class Product {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###");
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPromotionName() {
        return promotion.getName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return getPrice() == product.getPrice() && getQuantity() == product.getQuantity() && Objects.equals(getName(), product.getName()) && Objects.equals(promotion, product.promotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getQuantity(), promotion);
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return String.format("- %s %s원 재고 없음", name, FORMATTER.format(price));
        }
        return String.format("- %s %s원 %s개 %s", name, FORMATTER.format(price), FORMATTER.format(quantity), promotion.getName());
    }
}
