package store.view;

import store.entity.Product;

import java.util.List;

public abstract class OutputView {

    public static void printProductList(List<Product> products) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        products.forEach(System.out::println);
    }
}
