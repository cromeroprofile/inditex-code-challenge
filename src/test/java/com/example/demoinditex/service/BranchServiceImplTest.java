package com.example.demoinditex.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import com.example.demoinditex.Util;
import com.example.demoinditex.dto.Price;
import com.example.demoinditex.repository.PriceRepository;
import com.example.demoinditex.repository.domain.BranchEntity;
import com.example.demoinditex.repository.domain.PriceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private PriceRepository mockPriceRepository;

    @InjectMocks
    private BranchServiceImpl branchService;


    @Test
    void testGetPricesByBranchAndProductUniquePriceShouldReturnPrice() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        final var priceEntities = List.of(priceEntity);

        when(mockPriceRepository.findAllByBranchEntityIdAndProductId(priceEntity.getBranchEntity().getId(),priceEntity.getProductId() )).thenReturn(priceEntities);

        final var expectedResult = Optional.of(new Price(priceEntity.getPriceList(), priceEntity.getProductId(), priceEntity.getBranchEntity().getId(), priceEntity.getStartDate(), priceEntity.getEndDate(), priceEntity.getPrice(),
                        priceEntity.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(priceEntity.getBranchEntity().getId(), priceEntity.getProductId());

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetPricesByBranchAndProductNoPriceShouldReturnOptionalEmptyPrice() {
        // Setup

        final var priceEntities = new ArrayList<PriceEntity>();

        when(mockPriceRepository.findAllByBranchEntityIdAndProductId(0L, 0L)).thenReturn(priceEntities);

        final var expectedResult = Optional.empty();

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(0L, 0L);

        // Verify the results
        assertThat(result).isEmpty();
    }


    @Test
    void testGetPricesByBranchAndProductManyPricesAtSamePeriodShouldReturnHighestPriority() {
        // Setup

        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        PriceEntity priceEntity2 = Util.generatePriceEntity(2L,  branchEntity, 1);
        PriceEntity priceEntity3 = Util.generatePriceEntity( 3L, branchEntity, 5);

        final var priceEntities = List.of(priceEntity, priceEntity2, priceEntity3);

        when(mockPriceRepository.findAllByBranchEntityIdAndProductId(priceEntity.getBranchEntity().getId(),priceEntity.getProductId())).thenReturn(priceEntities);

        final var expectedResult = Optional.of(new Price(priceEntity.getPriceList(), priceEntity.getProductId(), priceEntity.getBranchEntity().getId(), priceEntity.getStartDate(), priceEntity.getEndDate(), priceEntity.getPrice(),
                priceEntity.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProduct(priceEntity.getBranchEntity().getId(), priceEntity.getProductId() );

        // Verify the results should return priceEntity with highest priority value
        assertThat(result).isEqualTo(expectedResult);
    }


    // ******** testGetPricesByBranchAndProductFilterByDate Tests ********


    @Test
    void testGetPricesByBranchAndProductFilterByDateUniquePriceShouldReturnPrice() {


        // Setup
        BranchEntity branchEntity = Util.generateBranchEntity(1L);
        PriceEntity priceEntity = Util.generatePriceEntity(1L,  branchEntity, 10);
        final var priceEntities = List.of(priceEntity);

        LocalDateTime localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        when(mockPriceRepository.findAllByBranchEntityIdAndProductIdAndDateIncluded(priceEntity.getBranchEntity().getId(),priceEntity.getProductId(),localDateTime )).thenReturn(priceEntities);

        final var expectedResult = Optional.of(new Price(priceEntity.getPriceList(), priceEntity.getProductId(), priceEntity.getBranchEntity().getId(), priceEntity.getStartDate(), priceEntity.getEndDate(), priceEntity.getPrice(),
                priceEntity.getCurrency()));

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(priceEntity.getBranchEntity().getId(), priceEntity.getProductId(),localDateTime );

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);

    }

    @Test
    void testGetPricesByBranchAndProductFilterByDateOutOfRangePriceShouldReturnOptionalEmptyPrice() {
        // Setup

        final var priceEntities = new ArrayList<PriceEntity>();

        LocalDateTime localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        when(mockPriceRepository.findAllByBranchEntityIdAndProductIdAndDateIncluded(1L, 1L,localDateTime )).thenReturn(priceEntities);

        // Run the test
        final Optional<Price> result = branchService.getPricesByBranchAndProductFilterByDate(1L, 1L,localDateTime );

        // Verify the results should return priceEntity with highest priority value
        assertThat(result).isEmpty();

    }


}
