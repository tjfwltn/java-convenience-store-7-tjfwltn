package store.controller;

import store.entity.*;
import store.service.PromotionService;
import store.util.*;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        }); // 사이다, 9

        PromotionProductMap purchaseProductMap = promotionService.calculatePromotions(purchaseProductList, productList);
        processGiftOptionByPromotion(purchaseProductMap, purchaseProductList);
        processPartialPaymentDecision(purchaseProductMap);
        int membershipDiscount = InputHandler.retryOnError(() -> {
            String answer = InputView.askApplyMemberShip();
            InputValidator.validateAnswerFormat(answer);
            int totalPrice = purchaseProductMap.getTotalPrice();
            if (answer.equals("Y")) {
                return MembershipDiscountCalculator.calculate(totalPrice);
            }
            return 0;
        });
//        OutputView.printReceipt(purchaseProductMap, membershipDiscount);
    }

    private void processGiftOptionByPromotion(PromotionProductMap purchaseProductMap, List<PurchaseProduct> purchaseProductList) {
        Map<Product, Integer> appliedPromotionMap = purchaseProductMap.getAppliedPromotionMap();
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        for (Map.Entry<Product, Integer> entry : appliedPromotionMap.entrySet()) {
            PurchaseProduct purchaseProduct = purchaseProductList.removeFirst();
            if (promotionService.canReceiveAdditionalProduct(entry.getKey(), purchaseProduct.getQuantity())) {
                InputHandler.retryOnError(() -> {
                    String answer = InputView.askAddGift(entry.getKey(), entry.getKey().getPromotion().getGiftAmount());
                    InputValidator.validateAnswerFormat(answer);
                    if (answer.equals("Y")) {
                        promotionService.calculateAndRemoveConflicts(entry, appliedPromotionMap, defaultPromotionMap);
                    }
                    return appliedPromotionMap;
                });
            }
        }
    }

    private void processPartialPaymentDecision(PromotionProductMap purchaseProductMap) {
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        if (!defaultPromotionMap.isEmpty()) {
            Iterator<Map.Entry<Product, Integer>> iterator = defaultPromotionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Product, Integer> entry = iterator.next();
                InputHandler.retryOnError(() -> {
                    String answer = InputView.askDefaultPromotionPurchase(entry.getKey(), entry.getValue());
                    if (answer.equals("N")) {
                        iterator.remove();
                    }
                    return purchaseProductMap;
                });
            }
        }
    }

}
