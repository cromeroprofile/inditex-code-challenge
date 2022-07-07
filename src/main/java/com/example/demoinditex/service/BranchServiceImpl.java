package com.example.demoinditex.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import com.example.demoinditex.dto.Price;
import com.example.demoinditex.repository.PriceRepository;
import com.example.demoinditex.repository.domain.PriceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService{

    private final PriceRepository priceRepository;

    // TODO ADD MAPSTRUCT --> AUTOMATIC MAPPER PER LAYER
    @Override
    public Optional<Price> getPricesByBranchAndProductFilterByDate(Long branchId, Long productId, LocalDateTime pricingDate) {
        return priceRepository.findAllByBranchEntityIdAndProductIdAndDateIncluded(branchId, productId, pricingDate).stream()
                .max(Comparator.comparing(PriceEntity::getPriority))
                .map(it -> new Price(it.getPriceList(), it.getProductId(),it.getBranchEntity().getId(), it.getStartDate(), it.getEndDate(),it.getPrice(), it.getCurrency()  ));
    }

    // TODO ADD MAPSTRUCT --> AUTOMATIC MAPPER PER LAYER
    @Override
    public Optional<Price> getPricesByBranchAndProduct(Long branchId, Long productId) {
        return priceRepository.findAllByBranchEntityIdAndProductId(branchId, productId).stream()
                .max(Comparator.comparing(PriceEntity::getPriority))
                .map(it -> new Price(it.getPriceList(), it.getProductId(),it.getBranchEntity().getId(), it.getStartDate(), it.getEndDate(),it.getPrice(), it.getCurrency()  ));
    }
}
