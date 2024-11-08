package store.service;

import store.domain.DefaultPromotion;
import store.domain.Promotion;
import store.entity.Product;
import store.entity.PromotionProductMap;
import store.entity.PurchaseProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionService {

    public PromotionProductMap calculatePromotions(List<PurchaseProduct> purchaseProductList, List<Product> productList) {
        Map<Product, Integer> appliedPromotionMap = new HashMap<>();
        Map<Product, Integer> defaultPromotionMap = new HashMap<>();

        purchaseProductList.forEach(purchaseProduct -> {
            Product promotionProduct = findPromotionProduct(productList, purchaseProduct);
            int purchaseQuantity = purchaseProduct.getQuantity();
            int promotionCount = calculatePromotionCount(promotionProduct, purchaseQuantity);
            appliedPromotionMap.put(promotionProduct, promotionCount);

            Product defaultProduct = findDefaultProduct(productList, purchaseProduct);
            int defaultCount = purchaseQuantity - promotionCount;
            defaultPromotionMap.put(defaultProduct, defaultCount);
        });
        return new PromotionProductMap(appliedPromotionMap, defaultPromotionMap);
    }

    private static Product findDefaultProduct(List<Product> productList, PurchaseProduct purchaseProduct) {
        return productList.stream()
                .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                .filter(product -> product.getPromotion().equals(new DefaultPromotion()))
                .findFirst()
                .get();
    }

    private static int calculatePromotionCount(Product promotionProduct, int purchaseQuantity) {
        Promotion promotion = promotionProduct.getPromotion();
        int purchaseAmount = promotion.getPurchaseAmount();
        int giftAmount = promotion.getGiftAmount();
        int totalAmount = purchaseAmount + giftAmount;
        return (purchaseQuantity / totalAmount) * totalAmount;
    }

    private static Product findPromotionProduct(List<Product> productList, PurchaseProduct purchaseProduct) {
        return productList.stream()
                .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                .filter(product -> !product.getPromotion().equals(new DefaultPromotion()))
                .findFirst()
                .get();
    }
}
