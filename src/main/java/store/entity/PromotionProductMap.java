package store.entity;

import java.util.HashMap;
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
        return Stream.concat(appliedPromotionMap.entrySet().stream(), defaultPromotionMap.entrySet().stream())
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public Map<String, Integer> getTotalAmountMap() {
        Map<String, Integer> totalAmountMap = new HashMap<>();
        appliedPromotionMap.forEach((key, value) -> totalAmountMap.put(key.getName(), value));
        defaultPromotionMap.forEach((key, value) ->
                totalAmountMap.merge(key.getName(), value, Integer::sum)
                );
        return totalAmountMap;
    }
}
