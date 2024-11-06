package store.view;

import camp.nextstep.edu.missionutils.Console;

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
}
