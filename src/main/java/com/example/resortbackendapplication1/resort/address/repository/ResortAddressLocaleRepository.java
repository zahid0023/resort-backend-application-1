package com.example.resortbackendapplication1.resort.address.repository;

import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortAddressLocaleRepository extends JpaRepository<@NonNull ResortAddressLocaleEntity, @NonNull Long> {

    Optional<ResortAddressLocaleEntity> findByResortAddressEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortAddressId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortAddressEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortAddressId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortAddressLocaleEntity> findByResortAddressEntity_IdAndIsActiveAndIsDeleted(
            Long resortAddressId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortAddressLocaleEntity> findByResortAddressEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortAddressId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select ral.localeEntity.code from ResortAddressLocaleEntity ral "
            + "where ral.resortAddressEntity.id = :resortAddressId "
            + "and ral.isActive = :isActive and ral.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortAddressEntity_IdAndIsActiveAndIsDeleted(
            Long resortAddressId, Boolean isActive, Boolean isDeleted);

}
