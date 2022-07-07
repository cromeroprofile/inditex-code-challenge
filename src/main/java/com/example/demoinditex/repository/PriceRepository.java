package com.example.demoinditex.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demoinditex.repository.domain.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    List<PriceEntity> findAllByBranchEntityIdAndProductId(Long branchEntityId, Long productId );

    @Query(value = "from PriceEntity p where p.branchEntity.id = :branchEntityId AND p.productId = :productId AND p.startDate <= :date AND p.endDate >= :date ")
    List<PriceEntity> findAllByBranchEntityIdAndProductIdAndDateIncluded(@Param("branchEntityId") Long branchEntityId, @Param("productId")Long productId, LocalDateTime date);


}
