package com.example.demoinditex.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price {

    private Long priceList;
    private Long productId;
    private Long branchEntityId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
    private Currency currency;

}
