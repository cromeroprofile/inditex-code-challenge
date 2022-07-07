package com.example.demoinditex.repository.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import lombok.Data;

@Entity
@Data
@Table(name="PRICES")
public class PriceEntity {

    @Id
    private Long priceList;
    @ManyToOne
    private BranchEntity branchEntity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long productId;
    private Integer priority;
    private BigDecimal price;
    private Currency currency;


}
