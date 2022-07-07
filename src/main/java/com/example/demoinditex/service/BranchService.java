package com.example.demoinditex.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.demoinditex.dto.Price;

public interface BranchService {

    Optional<Price> getPricesByBranchAndProductFilterByDate(Long branchId,
                                                            Long productId,
                                                            LocalDateTime pricingDate);

    Optional<Price> getPricesByBranchAndProduct(Long branchId, Long productId);


}
