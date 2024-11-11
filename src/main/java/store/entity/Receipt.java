package store.entity;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Receipt {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
    private final List<PurchaseProduct> purchaseProductList;
    private final PromotionProductMap purchaseProductMap;
    private final int membershipDiscount;

    public Receipt(List<PurchaseProduct> purchaseProductList, PromotionProductMap purchaseProductMap, int membershipDiscount) {
        this.purchaseProductList = purchaseProductList;
        this.purchaseProductMap = purchaseProductMap;
        this.membershipDiscount = membershipDiscount;
    }

    public Map<String, Integer> getGifts() {
        Map<String, Integer> giftMap = new LinkedHashMap<>();
        Map<Product, Integer> promotionMap = purchaseProductMap.appliedPromotionMap;
        for (Map.Entry<Product, Integer> entry : promotionMap.entrySet()) {
            int purchaseAmount = entry.getKey().getPromotion().getPurchaseAmount();
            int giftAmount = entry.getKey().getPromotion().getGiftAmount();
            String name = entry.getKey().getName();
            giftMap.put(name, entry.getValue() / (purchaseAmount + giftAmount));
        }
        return giftMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> gifts = getGifts();
        sb.append("==============W 편의점================\n");
        appendProductList(sb);
        sb.append("=============증\t\t정===============\n");
        appendGifts(gifts, sb);
        sb.append("====================================\n");
        appendFooter(sb);

        return sb.toString();
    }

    private void appendProductList(StringBuilder sb) {
        sb.append(String.format("%-16s %-8s %s%n", "상품명", "수량", "금액"));
        purchaseProductList.forEach(product ->
                sb.append(String.format("%-16s %-8d %s%n",
                        product.getProductName(), product.getQuantity(), DECIMAL_FORMAT.format(product.getPrice() * product.getQuantity())))
        );
    }

    private static void appendGifts(Map<String, Integer> gifts, StringBuilder sb) {
        gifts.forEach((key, value) ->
            sb.append(String.format("%-16s %-8d%n", key, value)));
    }

    private void appendFooter(StringBuilder sb) {
        sb.append(String.format("%-16s %-8d   %s%n",
                "총구매액", purchaseProductMap.getTotalAmount(), DECIMAL_FORMAT.format(purchaseProductMap.getTotalPrice())));
        int giftDiscount = purchaseProductMap.getGiftDiscount();
        sb.append(String.format("%-24s   -%s%n", "행사할인", DECIMAL_FORMAT.format(giftDiscount)));
        sb.append(String.format("%-24s   -%s%n", "멤버십할인", DECIMAL_FORMAT.format(membershipDiscount)));
        int finalAmount = purchaseProductMap.getTotalPrice() - giftDiscount - membershipDiscount;
        sb.append(String.format("%-24s    %s%n", "내실돈", DECIMAL_FORMAT.format(finalAmount)));
    }

}
