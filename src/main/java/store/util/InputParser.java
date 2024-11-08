package store.util;

import store.entity.PurchaseProduct;

import java.util.ArrayList;
import java.util.List;

public abstract class InputParser {

    public static List<PurchaseProduct> parse(String productStr) {
        List<PurchaseProduct> productList = new ArrayList<>();
        String[] products = productStr.split(",");
        for (String product : products) {
            String s = product.replaceAll("[\\[\\]]", "");
            String[] parts = s.split("-");
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            productList.add(new PurchaseProduct(productName, quantity));
        }
        return productList;
    }
}
