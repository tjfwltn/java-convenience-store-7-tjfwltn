package store.util;

import store.domain.Promotion;
import store.domain.PromotionFactory;
import store.entity.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {

    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final int FIRST_LINE = 1;
    private static final String SEPARATOR = ",";

    public static List<Product> parseProducts() throws IOException {
        return Files.lines(Path.of(PRODUCTS_FILE_PATH))
                .skip(FIRST_LINE)
                .map(line -> line.split(SEPARATOR))
                .map(fields -> new Product(
                        fields[0],
                        Integer.parseInt(fields[1]),
                        Integer.parseInt(fields[2]),
                        PromotionFactory.findMatchingPromotion(fields[3])
                )).collect(Collectors.toList());
    }

    public static List<Promotion> parsePromotions() throws IOException {
        return Files.lines(Path.of(PROMOTIONS_FILE_PATH))
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
