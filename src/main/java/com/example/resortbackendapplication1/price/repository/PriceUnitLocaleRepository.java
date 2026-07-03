package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceUnitLocaleRepository extends JpaRepository<@NonNull PriceUnitLocaleEntity, @NonNull Long> {

    Optional<PriceUnitLocaleEntity> findByPriceUnitEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long priceUnitId, Long id, Boolean isActive, Boolean isDeleted);
}
