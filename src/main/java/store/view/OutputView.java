package store.view;

import store.entity.Product;
import store.entity.PromotionProductMap;
import store.entity.PurchaseProduct;
import store.entity.Receipt;

import java.text.DecimalFormat;
import java.util.List;

public abstract class OutputView {

    public static void printProductList(List<Product> products) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        lineBreaking();
        products.forEach(System.out::println);
        lineBreaking();
    }

    private static void lineBreaking() {
        System.out.println();
    }

    public static void printErrorMessage(String message) {
        System.out.println(message);
    }

    public static void printReceipt(Receipt receipt) {
        System.out.println(receipt);
    }

    public static void printReceipt(List<PurchaseProduct> purchaseProductList, PromotionProductMap purchaseProductMap, int membershipDiscount) {
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("==============W 편의점================");
        System.out.printf("%-16s %-8s %s%n", "상품명", "수량", "금액");
        for (PurchaseProduct product : purchaseProductList) {
            System.out.printf("%-16s %-8s %s%n",
                    product.getProductName(), product.getQuantity(), df.format(product.getPrice() * product.getQuantity()));
        }
        System.out.println("=============증\t\t정===============");

        System.out.println("====================================");
    }
}
