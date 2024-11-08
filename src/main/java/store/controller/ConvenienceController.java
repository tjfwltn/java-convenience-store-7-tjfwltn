package store.controller;

import store.domain.DefaultPromotion;
import store.domain.Promotion;
import store.entity.Product;
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
        Map<Product, Integer> productMapAppliedPromotion = new HashMap<>();
        Map<Product, Integer> productMapDefaultPromotion = new HashMap<>();
        purchaseProductList.forEach(purchaseProduct -> {
            Product promotionProduct = productList.stream()
                    .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                    .filter(product -> !product.getPromotion().equals(new DefaultPromotion()))
                    .findFirst()
                    .get();

            int purchaseQuantity = purchaseProduct.getQuantity();

            Promotion promotion = promotionProduct.getPromotion();
            int purchaseAmount = promotion.getPurchaseAmount();
            int giftAmount = promotion.getGiftAmount();
            int totalAmount = purchaseAmount + giftAmount;
            int multiply = promotionProduct.getQuantity() / totalAmount;
            int promotionCount = multiply * totalAmount;
            productMapAppliedPromotion.put(promotionProduct, promotionCount);

            Product defaultProduct = productList.stream()
                    .filter(product -> product.getName().equals(purchaseProduct.getProductName()))
                    .filter(product -> product.getPromotion().equals(new DefaultPromotion()))
                    .findFirst()
                    .get();
            Collection<Integer> values = productMapAppliedPromotion.values();
            for (Integer value : values) {
                productMapDefaultPromotion.put(defaultProduct, purchaseQuantity - value);
            }
        });
        System.out.println("productMapAppliedPromotion = " + productMapAppliedPromotion);
        System.out.println("productMapDefaultPromotion = " + productMapDefaultPromotion);
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
