package store.controller;

import store.entity.Stock;
import store.entity.Product;
import store.entity.PromotionProductMap;
import store.entity.PurchaseProduct;
import store.entity.Receipt;
import store.service.PromotionService;
import store.util.*;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static store.constants.PromotionConstants.NONE_PROMOTION_LIST;

public class ConvenienceController {

    private final PromotionService promotionService = new PromotionService();

    public void run() throws IOException {
        InputView.printWelcomeMessage();
        List<Product> productList = FileEditor.parseProducts();
        OutputView.printProductList(productList);
        Stock stock = new Stock(productList);

        List<PurchaseProduct> purchaseProductList = getValidatedPurchaseProductList(stock);

        PromotionProductMap purchaseProductMap = promotionService.calculatePromotions(purchaseProductList, productList);
        processGiftOptionByPromotion(purchaseProductMap, purchaseProductList);
        processPartialPaymentDecision(purchaseProductMap, purchaseProductList);
        int membershipDiscount = getMembershipDiscount(purchaseProductMap);

        Receipt receipt = new Receipt(purchaseProductList, purchaseProductMap, membershipDiscount);
        OutputView.printReceipt(receipt);
        checkAndRepeatPurchase(purchaseProductMap);
    }

    private List<PurchaseProduct> getValidatedPurchaseProductList(Stock stock) {
        return InputHandler.retryOnError(() -> {
            String inputPurchaseProducts = InputView.requestProductToPurchase();
            InputValidator.validateProductFormat(inputPurchaseProducts);
            List<PurchaseProduct> purchaseProducts = InputParser.parse(inputPurchaseProducts);
            ProductValidator.validatePurchaseProducts(stock, purchaseProducts);
            return purchaseProducts;
        });
    }

    private void processGiftOptionByPromotion(PromotionProductMap purchaseProductMap, List<PurchaseProduct> purchaseProductList) {
        Map<Product, Integer> appliedPromotionMap = purchaseProductMap.getAppliedPromotionMap();
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        Iterator<PurchaseProduct> iterator = purchaseProductList.iterator();

        appliedPromotionMap.forEach(((product, quantity) -> {
            purchaseProductList.stream()
                    .filter(purchaseProduct -> purchaseProduct.getProductName().equals(product.getName()))
                    .findFirst()
                    .ifPresent(purchaseProduct -> handleGift(purchaseProduct, product, appliedPromotionMap, defaultPromotionMap));
        }));
    }

    private void handleGift(PurchaseProduct purchaseProduct, Product product, Map<Product, Integer> appliedPromotionMap, Map<Product, Integer> defaultPromotionMap) {
        if (promotionService.canReceiveAdditionalProduct(product, purchaseProduct.getQuantity())) {
            InputHandler.retryOnError(() -> {
                String answer = InputView.askAddGift(product, product.getPromotion().getGiftAmount());
                InputValidator.validateAnswerFormat(answer);
                if (answer.equals("Y")) {
                    int quantity = purchaseProduct.getQuantity();
                    purchaseProduct.setQuantity(quantity + 1);
                    promotionService.calculateAndRemoveConflicts(Map.entry(product, purchaseProduct.getQuantity()), appliedPromotionMap, defaultPromotionMap);
                }
                return appliedPromotionMap;
            });
        }
    }

    private void processPartialPaymentDecision(PromotionProductMap purchaseProductMap, List<PurchaseProduct> purchaseProductList) {
        Map<Product, Integer> defaultPromotionMap = purchaseProductMap.getDefaultPromotionMap();
        if (defaultPromotionMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<Product, Integer>> iterator = defaultPromotionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Product, Integer> entry = iterator.next();
            List<String> nonePromotionList = NONE_PROMOTION_LIST;
            if (nonePromotionList.contains(entry.getKey().getName())) {
                continue;
            }
            if (entry.getKey().getPromotion().isPromotionDay()) {
                InputHandler.retryOnError(() -> {
                    String answer = InputView.askDefaultPromotionPurchase(entry.getKey(), entry.getValue());
                    if (answer.equals("N")) {
                        purchaseProductList.removeIf(purchaseProduct -> purchaseProduct.getProductName().equals(entry.getKey().getName()));
                        iterator.remove();
                    }
                    return purchaseProductMap;
                });
            }
        }
    }

    private Integer getMembershipDiscount(PromotionProductMap purchaseProductMap) {
        return InputHandler.retryOnError(() -> {
            String answer = InputView.askApplyMemberShip();
            InputValidator.validateAnswerFormat(answer);
            int defaultPrice = purchaseProductMap.getDefaultPrice();
            if (answer.equals("Y")) {
                return MembershipDiscountCalculator.calculate(defaultPrice);
            }
            return 0;
        });
    }

    private void checkAndRepeatPurchase(PromotionProductMap purchaseProductMap) throws IOException {
        Boolean isAnotherPurchaseRequested = InputHandler.retryOnError(() -> {
            String input = InputView.askForAnotherProduct();
            return input.equals("Y");
        });
        if (isAnotherPurchaseRequested) {
            FileEditor.saveStock(purchaseProductMap);
            run();
        }
    }
}
