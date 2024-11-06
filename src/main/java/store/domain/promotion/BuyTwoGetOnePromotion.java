package store.domain.promotion;

public class BuyTwoGetOnePromotion implements Promotion {

    private final String promotionName = "탄산2+1";

    @Override
    public String getName() {
        return promotionName;
    }

    @Override
    public int discount() {
        return 0;
    }
}
