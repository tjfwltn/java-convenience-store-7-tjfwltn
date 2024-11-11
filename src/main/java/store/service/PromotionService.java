package store.service;

import store.domain.DefaultPromotion;
import store.domain.Promotion;
import store.entity.Product;
import store.entity.PromotionProductMap;
import store.entity.PurchaseProduct;

import java.util.*;

public class PromotionService {

    public PromotionProductMap calculatePromotions(List<PurchaseProduct> purchaseProductList, List<Product> productList) {
        Map<Product, Integer> appliedPromotionMap = new LinkedHashMap<>();
        Map<Product, Integer> defaultPromotionMap = new LinkedHashMap<>();

        purchaseProductList.forEach(purchaseProduct -> {
            Product promotionProduct = findPromotionProduct(productList, purchaseProduct);
            purchaseProduct.setPrice(promotionProduct);
            int purchaseQuantity = purchaseProduct.getQuantity();
            int promotionCount = 0;
            if (promotionProduct.getPromotion().isPromotionDay()) {
                promotionCount = calculatePromotionCount(promotionProduct, purchaseQuantity);
                appliedPromotionMap.put(promotionProduct, promotionCount);
            }
            Product defaultProduct = findDefaultProduct(productList, purchaseProduct);
            int defaultCount = purchaseQuantity - promotionCount;
            if (defaultCount != 0) {
                defaultPromotionMap.put(defaultProduct, defaultCount);
            }
        });
        return new PromotionProductMap(appliedPromotionMap, defaultPromotionMap);
    }

    public boolean canReceiveAdditionalProduct(Product product, int purchaseQuantity) {
        Promotion promotion = product.getPromotion();
        int stock = product.getQuantity();
        int purchaseAmount = promotion.getPurchaseAmount();
        int giftAmount = promotion.getGiftAmount();
        int totalAmount = purchaseAmount + giftAmount;
        if (!promotion.isPromotionDay()) {
            return false;
        }
        if (purchaseQuantity >= stock) {
            return false;
        }
        int remainder = purchaseQuantity % totalAmount;
        return remainder >= purchaseAmount;
    }

    public void calculateAndRemoveConflicts(Map.Entry<Product, Integer> entry, Map<Product, Integer> appliedPromotionMap, Map<Product, Integer> defaultPromotionMap) {
        Promotion promotion = entry.getKey().getPromotion();
        int giftAmount = promotion.getGiftAmount();
        int purchaseAmount = promotion.getPurchaseAmount();
        appliedPromotionMap.put(entry.getKey(),
                appliedPromotionMap.getOrDefault(entry.getKey(), 0) + giftAmount + purchaseAmount);

        removeConflictingProducts(entry, defaultPromotionMap);
    }

    private static void removeConflictingProducts(Map.Entry<Product, Integer> entry, Map<Product, Integer> defaultPromotionMap) {
        Iterator<Map.Entry<Product, Integer>> iterator = defaultPromotionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Product, Integer> defaultEntry = iterator.next();
            Product productInDefault = defaultEntry.getKey();
            if (productInDefault.getName().equals(entry.getKey().getName())) {
                iterator.remove();
            }
        }
    }

    private Product findPromotionProduct(List<Product> productList, PurchaseProduct purchaseProduct) {
        return productList.stream()
                .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                .findFirst()
                .get();
    }

    private int calculatePromotionCount(Product promotionProduct, int purchaseQuantity) {
        Promotion promotion = promotionProduct.getPromotion();
        int purchaseAmount = promotion.getPurchaseAmount(); // 1, 2
        int giftAmount = promotion.getGiftAmount(); // 1, 1
        int totalAmount = purchaseAmount + giftAmount;
        int promotionCount = (purchaseQuantity / totalAmount) * totalAmount;
        int promotionStock = promotionProduct.getQuantity();
        if (promotionCount > promotionStock) {
            promotionCount = (promotionStock / totalAmount) * totalAmount;
        }
        return promotionCount;
    }

    private Product findDefaultProduct(List<Product> productList, PurchaseProduct purchaseProduct) {
        return productList.stream()
                .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                .filter(product -> product.getPromotion().equals(new DefaultPromotion()))
                .findFirst()
                .get();
    }
}
