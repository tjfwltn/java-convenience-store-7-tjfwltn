package store.util;

import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.entity.Product;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileParserTest {

    @Test
    void parsePromotionsTest() throws IOException {
        // given
        // when
        List<Promotion> promotionList = FileParser.parsePromotions();
        // then
        assertThat(promotionList).isEqualTo(List.of(
                new Promotion("탄산2+1", 2, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31")),
                new Promotion("MD추천상품", 1, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31")),
                new Promotion("반짝할인", 1, 1, LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
        ));
    }

    @Test
    void parseProductsTest() throws IOException {
        // given
        // when
        List<Product> productList = FileParser.parseProducts();
        // then
        assertThat(productList.size()).isEqualTo(16);
    }
}