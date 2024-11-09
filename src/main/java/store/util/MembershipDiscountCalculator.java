package store.util;

public abstract class MembershipDiscountCalculator {

    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8_000;

    public static int calculate(int totalPrice) {
        int discount = (int) (totalPrice * DISCOUNT_RATE);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }
}
