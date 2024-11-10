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
}
