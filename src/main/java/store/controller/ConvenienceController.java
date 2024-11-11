package store.controller;

import store.domain.DefaultPromotion;
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
            int defaultPrice = purchaseProductMap.getDefaultPrice();
            if (answer.equals("Y")) {
                return MembershipDiscountCalculator.calculate(defaultPrice);
            }
            return 0;
        });
        Receipt receipt = new Receipt(purchaseProductList, purchaseProductMap, membershipDiscount);
        OutputView.printReceipt(receipt);
//        OutputView.printReceipt(purchaseProductList, purchaseProductMap, membershipDiscount);
    }

    private void processGiftOptionByPromotion(PromotionProductMap purchaseProductMap, List<PurchaseProduct> purchaseProductList) {
        Map<Product, Integer> appliedPromotionMap = purchaseProductMap.getAppliedPromotionMap();
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        Iterator<PurchaseProduct> iterator = purchaseProductList.iterator();
        for (Map.Entry<Product, Integer> entry : appliedPromotionMap.entrySet()) {
            if (iterator.hasNext()) {
                PurchaseProduct purchaseProduct = iterator.next();
                if (promotionService.canReceiveAdditionalProduct(entry.getKey(), purchaseProduct.getQuantity())) {
                    InputHandler.retryOnError(() -> {
                        String answer = InputView.askAddGift(entry.getKey(), entry.getKey().getPromotion().getGiftAmount());
                        InputValidator.validateAnswerFormat(answer);
                        if (answer.equals("Y")) {
                            int quantity = purchaseProduct.getQuantity();
                            purchaseProduct.setQuantity(quantity + 1);
                            promotionService.calculateAndRemoveConflicts(entry, appliedPromotionMap, defaultPromotionMap);
                        }
                        return appliedPromotionMap;
                    });
                }
            }
        }
    }

    private void processPartialPaymentDecision(PromotionProductMap purchaseProductMap) {
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        if (!defaultPromotionMap.isEmpty()) {
            Iterator<Map.Entry<Product, Integer>> iterator = defaultPromotionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Product, Integer> entry = iterator.next();
                if (!entry.getKey().getPromotion().equals(new DefaultPromotion())) {
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
}
