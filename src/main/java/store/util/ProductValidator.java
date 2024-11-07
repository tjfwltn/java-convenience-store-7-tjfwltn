package store.util;

import store.entity.Stock;

import static store.constants.ErrorMessage.EXCEEDED_STOCK;
import static store.constants.ErrorMessage.NON_EXISTENT_PRODUCT;

public abstract class ProductValidator {

    public static void validatePurchaseProducts(Stock stock, String inputPurchaseProducts) {
        String[] products = inputPurchaseProducts.split(",");
        for (String productStr : products) {
            String s = productStr.replaceAll("[\\[\\]]", "");
            String[] parts = s.split("-");
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);

            validateProductExistence(stock, productName);
            validateStockAvailability(stock, productName,quantity);
        }
    }

    private static void validateProductExistence(Stock stock, String productName) {
        if (!stock.getStock().containsKey(productName)) {
            throw new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage());
        }
    }

    private static void validateStockAvailability(Stock stock, String productName, int quantity) {
        Integer availableQuantity = stock.getStock().get(productName);
        if (availableQuantity < quantity) {
            throw new IllegalArgumentException(EXCEEDED_STOCK.getMessage());
        }
    }
}
