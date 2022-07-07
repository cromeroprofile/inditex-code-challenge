package com.example.demoinditex.service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.demoinditex.Util;
import com.example.demoinditex.dto.Price;
import com.example.demoinditex.repository.BranchRepository;
import com.example.demoinditex.repository.PriceRepository;
import com.example.demoinditex.repository.domain.BranchEntity;
import com.example.demoinditex.repository.domain.PriceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class BranchServiceImplntegrationTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private BranchRepository branchRepository;


    @Autowired
    private BranchServiceImpl branchService;


    @Test
    void testGetPricesByBranchAndProductUniquePriceShouldReturnPrice() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity( 1L, branchEntity, 10);
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        priceEntity.setBranchEntity(savedBranch);
        PriceEntity savedPrice = priceRepository.save(priceEntity);

        final var expectedResult = Optional.of(new Price(savedPrice.getPriceList(), savedPrice.getProductId(), savedPrice.getBranchEntity().getId(), savedPrice.getStartDate(), savedPrice.getEndDate(), savedPrice.getPrice(),
                savedPrice.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(priceEntity.getBranchEntity().getId(), priceEntity.getProductId());

        // Verify the results should return priceEntity with highest priority value
        assertThat(result).isEqualTo(expectedResult);

    }

    @Test
    void testGetPricesByBranchAndProductNoPriceShouldReturnOptionalEmptyPrice() {

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(0L, 0L);

        // Verify the results
        assertThat(result).isEmpty();
    }

    @Test
    void testGetPricesByBranchAndProductManyPricesAtSamePeriodShouldReturnHighestPriority() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        PriceEntity priceEntity2 = Util.generatePriceEntity( 2L, branchEntity, 1);
        PriceEntity priceEntity3 = Util.generatePriceEntity( 3L, branchEntity, 5);

        priceEntity.setBranchEntity(savedBranch);
        priceEntity2.setBranchEntity(savedBranch);
        priceEntity3.setBranchEntity(savedBranch);

        PriceEntity savedPrice = priceRepository.save(priceEntity);
        priceRepository.save(priceEntity2);
        priceRepository.save(priceEntity3);


        final var expectedResult = Optional.of(new Price(savedPrice.getPriceList(), savedPrice.getProductId(), savedPrice.getBranchEntity().getId(), savedPrice.getStartDate(), savedPrice.getEndDate(), savedPrice.getPrice(),
                savedPrice.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(priceEntity.getBranchEntity().getId(), priceEntity.getProductId());

        // Verify the results should return priceEntity with highest priority value
        assertThat(priceRepository.count()).isEqualTo(3);
        assertThat(result).isEqualTo(expectedResult);
    }

    // ******** testGetPricesByBranchAndProductFilterByDate Tests ********

    @Test
    void testGetPricesByBranchAndProductFilterByDateUniquePriceShouldReturnPrice() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity( 1L, branchEntity, 10);
        priceEntity.setStartDate(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        priceEntity.setEndDate(LocalDateTime.of(2028, 1, 1, 0, 0, 0));
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        priceEntity.setBranchEntity(savedBranch);
        PriceEntity savedPrice = priceRepository.save(priceEntity);

        final var expectedResult = Optional.of(new Price(savedPrice.getPriceList(), savedPrice.getProductId(), savedPrice.getBranchEntity().getId(), savedPrice.getStartDate(), savedPrice.getEndDate(), savedPrice.getPrice(),
                savedPrice.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(priceEntity.getBranchEntity().getId(), priceEntity.getProductId(),  LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results should return priceEntity with highest priority value
        assertThat(result).isEqualTo(expectedResult);

    }

    @Test
    void testGetPricesByBranchAndProductFilterByDateOutOfRangePriceShouldReturnOptionalEmptyPrice() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        priceEntity.setStartDate(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        priceEntity.setEndDate(LocalDateTime.of(2018, 1, 1, 0, 0, 0));
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        priceEntity.setBranchEntity(savedBranch);
        PriceEntity savedPrice = priceRepository.save(priceEntity);

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(priceEntity.getBranchEntity().getId(), priceEntity.getProductId(),  LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results should return priceEntity with highest priority value
        assertThat(result).isEmpty();

    }

    @Test
    void testGetPricesByBranchAndProductFilterByDateNoPriceShouldReturnOptionalEmptyPrice() {
        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(0L, 0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results
        assertThat(result).isEmpty();
    }


    @Test
    void testGetPricesByBranchAndProductFilterByDateManyPricesAtSamePeriodShouldReturnHighestPriority() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        BranchEntity savedBranch = branchRepository.save(branchEntity);

        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        priceEntity.setStartDate(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        priceEntity.setEndDate(LocalDateTime.of(2028, 1, 1, 0, 0, 0));

        PriceEntity priceEntity2 = Util.generatePriceEntity(2L,  branchEntity, 1);
        priceEntity2.setStartDate(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        priceEntity2.setEndDate(LocalDateTime.of(2028, 1, 1, 0, 0, 0));

        PriceEntity priceEntity3 = Util.generatePriceEntity(3L,  branchEntity, 5);
        priceEntity3.setStartDate(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        priceEntity3.setEndDate(LocalDateTime.of(2028, 1, 1, 0, 0, 0));

        priceEntity.setBranchEntity(savedBranch);
        priceEntity2.setBranchEntity(savedBranch);
        priceEntity3.setBranchEntity(savedBranch);

        PriceEntity savedPrice = priceRepository.save(priceEntity);
        priceRepository.save(priceEntity2);
        priceRepository.save(priceEntity3);


        final var expectedResult = Optional.of(new Price(savedPrice.getPriceList(), savedPrice.getProductId(), savedPrice.getBranchEntity().getId(), savedPrice.getStartDate(), savedPrice.getEndDate(), savedPrice.getPrice(),
                savedPrice.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(priceEntity.getBranchEntity().getId(), priceEntity.getProductId(), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results should return priceEntity with highest priority value
        assertThat(priceRepository.count()).isEqualTo(3);
        assertThat(result).isEqualTo(expectedResult);
    }



}
