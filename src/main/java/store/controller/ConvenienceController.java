package store.controller;

import store.entity.Product;
import store.entity.Stock;
import store.util.FileParser;
import store.util.InputValidator;
import store.util.ProductConverter;
import store.util.ProductValidator;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ConvenienceController {

    public void run() throws IOException {
        InputView.printWelcomeMessage();
        List<Product> productList = FileParser.parseProducts();
        Stock stock = new Stock(productList);

        OutputView.printProductList(productList);
//        retryOnError(() -> {
//            String inputPurchaseProducts = InputView.requestProductToPurchase();
//            InputValidator.validateProductFormat(inputPurchaseProducts);
//            ProductValidator.validatePurchaseProducts(stock, inputPurchaseProducts);
//        });
    }

    private <T> T retryOnError(Supplier<T> inputAction) {
        while (true) {
            try {
                return inputAction.get();
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
