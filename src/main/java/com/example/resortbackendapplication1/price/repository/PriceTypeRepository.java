package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PriceTypeRepository extends
        JpaRepository<@NonNull PriceTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceTypeEntity> {

    Optional<PriceTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Optional<PriceTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
