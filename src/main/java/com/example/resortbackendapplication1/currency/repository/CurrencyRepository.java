package com.example.resortbackendapplication1.currency.repository;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface CurrencyRepository extends
        JpaRepository<@NonNull CurrencyEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull CurrencyEntity> {

    Optional<CurrencyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    boolean existsByNumericCodeAndIsActiveAndIsDeleted(String numericCode, Boolean isActive, Boolean isDeleted);

    @Query("select c.code from CurrencyEntity c where c.isActive = :isActive and c.isDeleted = :isDeleted")
    List<String> findCodeByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
