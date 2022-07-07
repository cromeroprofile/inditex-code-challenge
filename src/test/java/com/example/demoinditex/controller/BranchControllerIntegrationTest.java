package com.example.demoinditex.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.example.demoinditex.Util.PRODUCT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.demoinditex.Util;
import com.example.demoinditex.dto.Price;
import com.example.demoinditex.repository.BranchRepository;
import com.example.demoinditex.repository.PriceRepository;
import com.example.demoinditex.repository.domain.BranchEntity;
import com.example.demoinditex.repository.domain.PriceEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BranchControllerIntegrationTest {

    public static final String URL_LOCALHOST = "http://localhost";
    public static final BigDecimal PRICE1 = BigDecimal.valueOf(35.50);
    public static final BigDecimal PRICE2 = BigDecimal.valueOf(25.45);
    public static final BigDecimal PRICE3 = BigDecimal.valueOf(30.50);
    public static final BigDecimal PRICE4 = BigDecimal.valueOf(38.95);

    private static Stream<Arguments> PostConditionsAssertGenerator() {
        return Stream.of(
                Arguments.of("2020-06-14T10:00:00.000Z", PRICE1),
                Arguments.of("2020-06-14T16:00:00.000Z", PRICE2),
                Arguments.of("2020-06-14T21:00:00.000Z", PRICE1),
                Arguments.of("2020-06-15T10:00:00.000Z", PRICE3),
                Arguments.of("2020-06-16T21:00:00.000Z", PRICE4)
        );
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Value("${local.server.port}")
    private int port;


    @BeforeAll
    public void setup() {
        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity(1L, branchEntity, 10);
        priceEntity.setPriority(0);
        priceEntity.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0));
        priceEntity.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        priceEntity.setPrice(PRICE1);
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        priceEntity.setBranchEntity(savedBranch);
        priceRepository.save(priceEntity);


        PriceEntity priceEntity2 = Util.generatePriceEntity(2L, branchEntity, 10);
        priceEntity2.setPriority(1);
        priceEntity2.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0, 0));
        priceEntity2.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30, 0));
        priceEntity2.setPrice(PRICE2);
        priceEntity2.setBranchEntity(savedBranch);
        priceRepository.save(priceEntity2);

        PriceEntity priceEntity3 = Util.generatePriceEntity(3L, branchEntity, 10);
        priceEntity3.setPriority(1);
        priceEntity3.setStartDate(LocalDateTime.of(2020, 6, 15, 0, 0, 0));
        priceEntity3.setEndDate(LocalDateTime.of(2020, 6, 15, 11, 0, 0));
        priceEntity3.setPrice(PRICE3);
        priceEntity3.setBranchEntity(savedBranch);
        priceRepository.save(priceEntity3);

        PriceEntity priceEntity4 = Util.generatePriceEntity(4L, branchEntity, 10);
        priceEntity4.setPriority(1);
        priceEntity4.setStartDate(LocalDateTime.of(2020, 6, 15, 16, 0, 0));
        priceEntity4.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        priceEntity4.setPrice(PRICE4);
        priceEntity4.setBranchEntity(savedBranch);
        priceRepository.save(priceEntity4);
    }

    @ParameterizedTest
    @MethodSource("PostConditionsAssertGenerator")
    void whenCallingGetBranchUsingDateFilterShouldReturnCorrectValues(String date, BigDecimal expectedPrice) {

        Price price = restTemplate.getForObject(
                URL_LOCALHOST + ":" + port + "/api/v1/branches/{branchId}/products/{productId}?pricingDate=" + date, Price.class, 1L, PRODUCT_ID);

        assertThat(price).isNotNull();
        assertThat(price.getPrice()).isEqualByComparingTo(expectedPrice);

    }

    @Test
    void whenCallingGetBranchNoDateFilterShouldReturnCorrectValue() {

        Price price = restTemplate.getForObject(
                URL_LOCALHOST + ":" + port + "/api/v1/branches/{branchId}/products/{productId}", Price.class, 1L, PRODUCT_ID);

        assertThat(price).isNotNull();
        assertThat(price.getPriceList()).isEqualTo(2L);

    }

}
