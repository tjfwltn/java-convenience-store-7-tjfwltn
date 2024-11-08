package store.entity;

import java.util.Map;

public class PromotionProductMap {

    Map<Product, Integer> appliedPromotionMap;
    Map<Product, Integer> defaultPromotionMap;

    public PromotionProductMap(Map<Product, Integer> appliedPromotionMap, Map<Product, Integer> defaultPromotionMap) {
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
