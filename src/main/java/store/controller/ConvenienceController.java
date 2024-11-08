package store.controller;

import store.domain.DefaultPromotion;
import store.domain.Promotion;
import store.entity.Product;
import store.entity.PromotionResult;
import store.entity.PurchaseProduct;
import store.entity.Stock;
import store.service.PromotionService;
import store.util.FileParser;
import store.util.InputParser;
import store.util.InputValidator;
import store.util.ProductValidator;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public class ConvenienceController {

    private final PromotionService promotionService = new PromotionService();

    public void run() throws IOException {
        InputView.printWelcomeMessage();
        List<Product> productList = FileParser.parseProducts();
        Stock stock = new Stock(productList);

        OutputView.printProductList(productList);
        List<PurchaseProduct> purchaseProductList = retryOnError(() -> {
            String inputPurchaseProducts = InputView.requestProductToPurchase();
            InputValidator.validateProductFormat(inputPurchaseProducts);
            List<PurchaseProduct> purchaseProducts = InputParser.parse(inputPurchaseProducts);
            ProductValidator.validatePurchaseProducts(stock, purchaseProducts);
            return purchaseProducts;
        });
        PromotionResult promotionResult = promotionService.calculatePromotions(purchaseProductList, productList);
        System.out.println("promotionResult.getAppliedPromotionMap() = " + promotionResult.getAppliedPromotionMap());
        System.out.println("promotionResult.getDefaultPromotionMap() = " + promotionResult.getDefaultPromotionMap());
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
