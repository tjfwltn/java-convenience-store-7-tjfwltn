package store.controller;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Promotion;
import store.entity.Product;
import store.entity.PromotionProductMap;
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
        List<PurchaseProduct> purchaseProductList = retryOnError(() -> {
            String inputPurchaseProducts = InputView.requestProductToPurchase();
            InputValidator.validateProductFormat(inputPurchaseProducts);
            List<PurchaseProduct> purchaseProducts = InputParser.parse(inputPurchaseProducts);
            ProductValidator.validatePurchaseProducts(stock, purchaseProducts);
            return purchaseProducts;
        });
        PromotionProductMap purchaseProductMap = promotionService.calculatePromotions(purchaseProductList, productList);
        processGiftOptionByPromotion(purchaseProductMap, purchaseProductList);
        processPartialPaymentDecision(purchaseProductMap);
        System.out.println("할인 맵 = " + purchaseProductMap.getAppliedPromotionMap());
        System.out.println("디폴트 맵 = " + purchaseProductMap.getDefaultPromotionMap());
    }

    private void processPartialPaymentDecision(PromotionProductMap purchaseProductMap) {
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        if (!defaultPromotionMap.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : defaultPromotionMap.entrySet()) {
                retryOnError(() -> {
                    String input = InputView.askDefaultPromotionPurchase(entry.getKey(), entry.getValue());
                    if (input.equals("N")) {
                        defaultPromotionMap.remove(entry.getKey());
                    }
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
                retryOnError(() -> {
                    String input = InputView.askAddGift(entry.getKey(), entry.getKey().getPromotion().getGiftAmount());
                    InputValidator.validateAnswerFormat(input);
                    promotionService.calculateAndRemoveConflicts(entry, input, appliedPromotionMap, defaultPromotionMap);
                    return appliedPromotionMap;
                });
            }
        }
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
