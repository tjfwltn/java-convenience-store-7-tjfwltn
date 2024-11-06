package store.domain.promotion;

public class BuyOneGetOnePromotion implements Promotion {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public int discount() {
        return 0;
    }
}
