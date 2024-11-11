package store.util;

import store.entity.PurchaseProduct;
import store.domain.Stock;

import java.util.List;

import static store.constants.ErrorMessage.EXCEEDED_STOCK;
import static store.constants.ErrorMessage.NON_EXISTENT_PRODUCT;

public abstract class ProductValidator {

    public static void validatePurchaseProducts(Stock stock, List<PurchaseProduct> purchaseProductList) {
        for (PurchaseProduct purchaseProduct : purchaseProductList) {
            String productName = purchaseProduct.getProductName();
            int quantity = purchaseProduct.getQuantity();

            validateProductExistence(stock, productName);
            validateStockAvailability(stock, productName, quantity);
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
