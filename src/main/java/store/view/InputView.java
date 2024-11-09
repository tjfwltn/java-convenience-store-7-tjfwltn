package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.entity.Product;

import java.util.Map;

import static store.constants.ViewMessage.*;

public abstract class InputView {

    public static void printWelcomeMessage() {
        System.out.println(WELCOME);
    }

    public static String requestProductToPurchase() {
        System.out.println(REQUEST_PRODUCT_TO_PURCHASE);
        return Console.readLine();
    }

    public static String askApplyMemberShip() {
        System.out.println(ASK_APPLY_MEMBERSHIP);
        return Console.readLine();
    }

    public static String askForAnotherProduct() {
        System.out.println(ASK_FOR_ANOTHER_PRODUCT);
        return Console.readLine();
    }

    public static String askAddGift(Product key, int giftAmount) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n",
                key.getName(), giftAmount);
        return Console.readLine();
    }

    public static String askDefaultPromotionPurchase(Product key, Integer value) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n",
                key.getName(), value);
        return Console.readLine();
    }
}
