package store.entity;

import java.util.Map;

public class PromotionResult {

    private final Map<Product, Integer> appliedPromotionMap;
    private final Map<Product, Integer> defaultPromotionMap;

    public PromotionResult(Map<Product, Integer> appliedPromotionMap, Map<Product, Integer> defaultPromotionMap) {
        this.appliedPromotionMap = appliedPromotionMap;
        this.defaultPromotionMap = defaultPromotionMap;
    }

    public Map<Product, Integer> getAppliedPromotionMap() {
        return appliedPromotionMap;
    }

    public Map<Product, Integer> getDefaultPromotionMap() {
        return defaultPromotionMap;
    }
}
