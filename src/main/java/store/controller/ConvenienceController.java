package store.controller;

import store.entity.Product;
import store.entity.PromotionProductMap;
import store.entity.PurchaseProduct;
import store.entity.Stock;
import store.service.PromotionService;
import store.util.*;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ConvenienceController {

    private final PromotionService promotionService = new PromotionService();

    public void run() throws IOException {
        InputView.printWelcomeMessage();
        List<Product> productList = FileParser.parseProducts();
        OutputView.printProductList(productList);
        Stock stock = new Stock(productList);
        List<PurchaseProduct> purchaseProductList = InputHandler.retryOnError(() -> {
            String inputPurchaseProducts = InputView.requestProductToPurchase();
            InputValidator.validateProductFormat(inputPurchaseProducts);
            List<PurchaseProduct> purchaseProducts = InputParser.parse(inputPurchaseProducts);
            ProductValidator.validatePurchaseProducts(stock, purchaseProducts);
            return purchaseProducts;
        });
        PromotionProductMap purchaseProductMap = promotionService.calculatePromotions(purchaseProductList, productList);
        processGiftOptionByPromotion(purchaseProductMap, purchaseProductList);
        processPartialPaymentDecision(purchaseProductMap);
//        InputHandler.retryOnError(() -> {
//            String answer = InputView.askApplyMemberShip();
//            InputValidator.validateAnswerFormat(answer);
//            if ()
//        });
        System.out.println("purchaseProductMap.getAppliedPromotionMap() = " + purchaseProductMap.getAppliedPromotionMap());
        System.out.println("purchaseProductMap.getDefaultPromotionMap() = " + purchaseProductMap.getDefaultPromotionMap());
    }

    private void processPartialPaymentDecision(PromotionProductMap purchaseProductMap) {
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        if (!defaultPromotionMap.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : defaultPromotionMap.entrySet()) {
                InputHandler.retryOnError(() -> {
                    String input = InputView.askDefaultPromotionPurchase(entry.getKey(), entry.getValue());
                    InputHandler.processInputNo(input, () -> defaultPromotionMap.remove(entry.getKey()));
                    return purchaseProductMap;
                });
            }
        }
    }

    private void processGiftOptionByPromotion(PromotionProductMap purchaseProductMap, List<PurchaseProduct> purchaseProductList) {
        Map<Product, Integer> appliedPromotionMap = purchaseProductMap.getAppliedPromotionMap();
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        for (Map.Entry<Product, Integer> entry : appliedPromotionMap.entrySet()) {
            PurchaseProduct purchaseProduct = purchaseProductList.removeFirst();
            if (promotionService.canReceiveAdditionalProduct(entry.getKey(), purchaseProduct.getQuantity())) {
                InputHandler.retryOnError(() -> {
                    String input = InputView.askAddGift(entry.getKey(), entry.getKey().getPromotion().getGiftAmount());
                    InputValidator.validateAnswerFormat(input);
                    InputHandler.processInputYes(input,
                            () -> promotionService.calculateAndRemoveConflicts(entry, appliedPromotionMap, defaultPromotionMap)
                    );
                    return appliedPromotionMap;
                });
            }
        }
    }

}
