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

        for (PurchaseProduct product : purchaseProductList) {
            sb.append(String.format("%-16s %-8d %s%n",
                    product.getProductName(), product.getQuantity(), df.format(product.getPrice() * product.getQuantity())));
        }
        sb.append("=============증\t\t정===============\n");
        for (Map.Entry<String, Integer> entry : gifts.entrySet()) {
            sb.append(String.format("%-16s %-8d%n", entry.getKey(), entry.getValue()));
        }
        sb.append("====================================\n");
        sb.append(String.format("%-16s %-8d %s%n",
                        "총구매액", purchaseProductMap.getTotalAmount(), df.format(purchaseProductMap.getTotalPrice())));
        sb.append(String.format("%-24s  %s%n", "행사할인", ))
        return sb.toString();
    }
}
