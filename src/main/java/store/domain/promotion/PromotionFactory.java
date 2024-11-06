package store.domain.promotion;

public class PromotionFactory {

    private static final String NULL = "null";

    public static Promotion getPromotion(String promotionName) {
        if (promotionName.equals(NULL)) {
            return new DefaultPromotion();
        }

        if (promotionName.equals())
    }
}
