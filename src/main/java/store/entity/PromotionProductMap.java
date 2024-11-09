package store.entity;

import java.util.Map;
import java.util.stream.Stream;

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

    public int getTotalPrice() {
        return Stream.concat(
                appliedPromotionMap.values().stream(),
                defaultPromotionMap.values().stream())
                .mapToInt(Integer::intValue)
                .sum();
    }
}
