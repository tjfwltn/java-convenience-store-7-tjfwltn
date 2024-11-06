package store.util;

import store.entity.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StockParser {

    private static final String FILE_PATH = "src/main/resources/products.md";
    private static final int FIRST_LINE = 1;
    private static final String SEPARATOR = ",";

    public static List<Product> parse() throws IOException {
        Path path = Path.of(FILE_PATH);
        Files.lines(path)
                .skip(FIRST_LINE)
                .forEach(line -> {
                    String[] parts = line.split(SEPARATOR);

                });

    }
}
