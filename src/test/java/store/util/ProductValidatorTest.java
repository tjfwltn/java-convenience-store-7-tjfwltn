package store.util;

import org.junit.jupiter.api.Test;
import store.entity.Product;
import store.entity.PurchaseProduct;
import store.entity.Stock;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static store.constants.ErrorMessage.*;

class ProductValidatorTest {

    @Test
    void validateProductExistenceTest() throws IOException {
        // given
        List<Product> productList = FileEditor.parseProducts();
        Stock stock = new Stock(productList);
        List<PurchaseProduct> purchaseProductList = List.of(
                new PurchaseProduct("마운틴듀", 21)
        );
        // when & then
        assertThatThrownBy(() -> ProductValidator.validatePurchaseProducts(stock, purchaseProductList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NON_EXISTENT_PRODUCT.getMessage());
    }

    @Test
    void validateStockAvailabilityTest() throws IOException {
        // given
        List<Product> productList = FileEditor.parseProducts();
        Stock stock = new Stock(productList);
        List<PurchaseProduct> purchaseProductList = List.of(
                new PurchaseProduct("콜라", 21)
        );
        // when & then
        assertThatThrownBy(() -> ProductValidator.validatePurchaseProducts(stock, purchaseProductList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EXCEEDED_STOCK.getMessage());
    }
}