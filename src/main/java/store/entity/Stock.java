package store.entity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stock {
    private final Map<String, Integer> stock;

    public Stock(List<Product> productList) {
        this.stock = productList.stream()
                .collect(Collectors.groupingBy(
                        Product::getName,
                        Collectors.summingInt(Product::getQuantity)
                ));
    }

    public Map<String, Integer> getStock() {
        return stock;
    }
}
