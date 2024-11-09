package store.util;

import store.constants.ErrorMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static store.constants.ErrorMessage.*;

public abstract class InputValidator {

    private static final String PRODUCT_PATTERN = "(\\[[가-힣\\s]+-\\d+\\])(,(\\[[가-힣\\s]+-\\d+\\]))*";

    public static void validateProductFormat(String product) {
        Pattern pattern = Pattern.compile(PRODUCT_PATTERN);
        Matcher matcher = pattern.matcher(product);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    public static void validateAnswerFormat(String answer) {
        if (!"Y".equals(answer) && !"N".equals(answer)) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}
