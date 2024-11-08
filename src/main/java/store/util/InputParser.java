package store.util;

import store.constants.ErrorMessage;
import store.entity.PurchaseProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static store.constants.ErrorMessage.*;

public abstract class InputParser {

    public static List<PurchaseProduct> parse(String productStr) {
        List<PurchaseProduct> productList = new ArrayList<>();
        Map<String, Integer> productNameMap = new HashMap<>();
        String[] products = productStr.split(",");
        for (String product : products) {
            String s = product.replaceAll("[\\[\\]]", "");
            String[] parts = s.split("-");
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            validatesDuplicates(productNameMap, productName, quantity);
            productList.add(new PurchaseProduct(productName, quantity));
        }
        return productList;
    }

    private static void validatesDuplicates(Map<String, Integer> productNameMap, String productName, int quantity) {
        if (productNameMap.containsKey(productName)) {
            throw new IllegalArgumentException(DEFAULT_INVALID_INPUT.getMessage());
        }
        productNameMap.put(productName, quantity);
    }
}
