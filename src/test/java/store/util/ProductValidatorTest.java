package store.util;

import org.junit.jupiter.api.Test;
import store.constants.ErrorMessage;
import store.entity.Product;
import store.entity.Stock;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static store.constants.ErrorMessage.*;

class ProductValidatorTest {

    @Test
    void validateProductExistenceTest() throws IOException {
        // given
        List<Product> productList = FileParser.parseProducts();
        Stock stock = new Stock(productList);
        String input = "[마운틴듀-10]";
        // when & then
        assertThatThrownBy(() -> ProductValidator.validatePurchaseProducts(stock, input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NON_EXISTENT_PRODUCT.getMessage());
    }

    @Test
    void validateStockAvailabilityTest() throws IOException {
        // given
        List<Product> productList = FileParser.parseProducts();
        Stock stock = new Stock(productList);
        String input = "[콜라-21]";
        // when & then
        assertThatThrownBy(() -> ProductValidator.validatePurchaseProducts(stock, input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EXCEEDED_STOCK.getMessage());
    }
}