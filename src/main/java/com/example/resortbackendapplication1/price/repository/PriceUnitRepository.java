package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceUnitRepository extends
        JpaRepository<@NonNull PriceUnitEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceUnitEntity> {

    Optional<PriceUnitEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
