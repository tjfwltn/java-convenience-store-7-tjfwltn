package store.view;

import store.entity.Product;

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

//    public static void printReceipt(PromotionProductMap purchaseProductMap, int membershipDiscount) {
//        System.out.println("==============W 편의점================");
//        System.out.printf("%-10s %-10s %-10s%n", "상품명", "수량", "금액");
//        System.out.printf("%-10s %-10s %-10s%n", "상품명", "수량", "금액");
//        System.out.printf("%-10s %-10d %-10d%n", productName, quantity, price);
//    }
}
