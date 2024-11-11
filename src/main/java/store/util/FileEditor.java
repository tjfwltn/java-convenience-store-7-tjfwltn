package store.util;

import store.domain.Promotion;
import store.domain.PromotionFactory;
import store.entity.Product;
import store.entity.PromotionProductMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileEditor {

    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final int FIRST_LINE = 1;
    private static final String SEPARATOR = ",";

    public static List<Product> parseProducts() throws IOException {
        try (var lines = Files.lines(Path.of(PRODUCTS_FILE_PATH))) {
            return lines
                    .skip(FIRST_LINE)
                    .map(line -> line.split(SEPARATOR))
                    .map(fields -> new Product(
                            fields[0],
                            Integer.parseInt(fields[1]),
                            Integer.parseInt(fields[2]),
                            PromotionFactory.findMatchingPromotion(fields[3])
                    )).collect(Collectors.toList());
        }
    }

    public static List<Promotion> parsePromotions() throws IOException {
        try (var lines = Files.lines(Path.of(PROMOTIONS_FILE_PATH))) {
            return lines
                    .skip(FIRST_LINE)
                    .map(line -> line.split(SEPARATOR))
                    .map(fields -> new Promotion(
                            fields[0],
                            Integer.parseInt(fields[1]),
                            Integer.parseInt(fields[2]),
                            LocalDate.parse(fields[3]),
                            LocalDate.parse(fields[4])
                    ))
                    .collect(Collectors.toList());
        }
    }

    public static void saveStock(PromotionProductMap purchaseProductMap) throws IOException {
        Path path = Path.of(PRODUCTS_FILE_PATH);
        List<String> lines = Files.readAllLines(path);

        String header = lines.getFirst();
        List<String> dataLines = lines.subList(1, lines.size());

        Map<Product, Integer> appliedPromotionMap = purchaseProductMap.getAppliedPromotionMap();
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();

        List<String> updatedLines = dataLines.stream()
                .map(line -> updateLine(line, appliedPromotionMap, defaultPromotionMap))
                .collect(Collectors.toList());

        updatedLines.addFirst(header);
        Files.write(path, updatedLines);
    }

    private static String updateLine(String line, Map<Product, Integer> appliedPromotionMap, Map<Product, Integer> defaultPromotionMap) {
        String[] fields = line.split(SEPARATOR);
        String productName = fields[0];
        int quantity = Integer.parseInt(fields[2]);

        quantity -= getReductionQuantity(productName, fields[3], appliedPromotionMap);
        quantity -= getReductionQuantity(productName, fields[3], defaultPromotionMap);
        quantity = Math.max(quantity, 0);

        fields[2] = String.valueOf(quantity);
        return String.join(SEPARATOR, fields);
    }

    private static int getReductionQuantity(String productName, String promotionName, Map<Product, Integer> map) {
        return map.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(productName)
                && entry.getKey().getPromotion().getName().equals(promotionName))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}
