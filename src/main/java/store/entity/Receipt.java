package store.entity;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Receipt {

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
        DecimalFormat df = new DecimalFormat("#,###");
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> gifts = getGifts();
        sb.append("==============W 편의점================\n");
        sb.append(String.format("%-16s %-8s %s%n", "상품명", "수량", "금액"));

        purchaseProductList.forEach(product ->
            sb.append(String.format("%-16s %-8d %s%n",
                    product.getProductName(), product.getQuantity(), df.format(product.getPrice() * product.getQuantity())))
        );
        sb.append("=============증\t\t정===============\n");
        gifts.forEach((key, value) ->
            sb.append(String.format("%-16s %-8d%n", key, value)));
        sb.append("====================================\n");
        sb.append(String.format("%-16s %-8d   %s%n",
                "총구매액", purchaseProductMap.getTotalAmount(), df.format(purchaseProductMap.getTotalPrice())));
        int giftDiscount = purchaseProductMap.getGiftDiscount();
        sb.append(String.format("%-24s   -%s%n", "행사할인", df.format(giftDiscount)));
        sb.append(String.format("%-24s   -%s%n", "멤버십할인", df.format(membershipDiscount)));
        int finalAmount = purchaseProductMap.getTotalPrice() - giftDiscount - membershipDiscount;
        sb.append(String.format("%-24s    %s%n", "내실돈", df.format(finalAmount)));

        return sb.toString();
    }

}
