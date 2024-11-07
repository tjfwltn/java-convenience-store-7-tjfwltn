package store.util;

import store.constants.ErrorMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class InputValidator {

    private static final String PRODUCT_PATTERN = "(\\[[가-힣\\s]+-\\d+\\])(,(\\[[가-힣\\s]+-\\d+\\]))*";

    public static void validateProductFormat(String product) {
        Pattern pattern = Pattern.compile(PRODUCT_PATTERN);
        Matcher matcher = pattern.matcher(product);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }
}
