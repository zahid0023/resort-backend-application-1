package com.example.resortbackendapplication1.price.repository;

import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public interface PriceScopeRepository extends
        JpaRepository<@NonNull PriceScopeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PriceScopeEntity> {

    Optional<PriceScopeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<@NonNull PriceScopeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    @Query("select p.code from PriceScopeEntity p where p.isActive = :isActive and p.isDeleted = :isDeleted")
    List<String> findCodeByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);

}
