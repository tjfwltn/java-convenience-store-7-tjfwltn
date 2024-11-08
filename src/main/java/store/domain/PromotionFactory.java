package store.domain;

import store.util.FileParser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PromotionFactory {

    private static final List<Promotion> promotionList;

    static {
        try {
            promotionList = FileParser.parsePromotions();
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
