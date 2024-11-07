package store.controller;

import store.entity.Product;
import store.util.FileParser;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;

public class ConvenienceController {

    public void run() throws IOException {
        List<Product> products = FileParser.parseProducts();
        OutputView.printProductList(products);
    }
}
