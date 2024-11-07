package store.util;

import store.domain.Promotion;
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

//    public static List<Product> parseProducts() throws IOException {
//        Files.lines(Path.of(PRODUCTS_FILE_PATH))
//                .skip(FIRST_LINE)
//                .forEach(line -> {
//                    String[] parts = line.split(SEPARATOR);
//                    String name =
//                })
//
//    }

    public static List<Promotion> parsePromotions() throws IOException {
        return Files.lines(Path.of(PROMOTIONS_FILE_PATH))
                .skip(FIRST_LINE)
                .map(line -> {
                    String[] promotionFields = line.split(SEPARATOR);
                    String name = promotionFields[0];
                    int buy = Integer.parseInt(promotionFields[1]);
                    int get = Integer.parseInt(promotionFields[2]);
                    LocalDate startDate = LocalDate.parse(promotionFields[3]);
                    LocalDate endDate = LocalDate.parse(promotionFields[4]);

                    return new Promotion(name, buy, get, startDate, endDate);
                })
                .collect(Collectors.toList());
    }
}
