package store.view;

import store.entity.Product;
import store.entity.Receipt;

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

}
