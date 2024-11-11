package store.domain;

import store.util.FileEditor;

import java.io.IOException;
import java.util.List;

public abstract class PromotionFactory {

    private static final List<Promotion> promotionList;

    static {
        try {
            promotionList = FileEditor.parsePromotions();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Promotion findMatchingPromotion(String promotionName) {
        return promotionList.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .orElse(new DefaultPromotion());
    }
}
