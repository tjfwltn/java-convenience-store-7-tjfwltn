package store.controller;

import store.entity.Product;
import store.util.FileParser;
import store.util.InputValidator;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;

public class ConvenienceController {

    public void run() throws IOException {
        InputView.printWelcomeMessage();
        List<Product> products = FileParser.parseProducts();
        OutputView.printProductList(products);
        while (true) {
            try {
                String inputPurchaseProducts = InputView.requestProductToPurchase();
                InputValidator.validateProductFormat(inputPurchaseProducts);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
