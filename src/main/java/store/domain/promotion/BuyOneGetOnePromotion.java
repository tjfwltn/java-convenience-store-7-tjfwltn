package store.domain.promotion;

public class BuyOneGetOnePromotion implements Promotion {

    private String promotionName;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int discount() {
        return 0;
    }
}
