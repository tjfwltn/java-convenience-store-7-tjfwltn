package store.util;

import store.view.OutputView;

import java.util.function.Supplier;

public abstract class InputHandler {

    public static  <T> T retryOnError(Supplier<T> inputAction) {
        while (true) {
            try {
                return inputAction.get();
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    public static void processInputYes(String input, Runnable yesAction) {
        if (input.equals("Y")) {
            yesAction.run();
        }
    }

    public static void processInputNo(String input, Runnable noAction) {
        if (input.equals("N")) {
            noAction.run();
        }
    }
}
