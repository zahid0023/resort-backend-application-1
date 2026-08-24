package com.example.resortbackendapplication1.resortroletype.repository;

import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoleTypeLocaleRepository extends
        JpaRepository<@NonNull ResortRoleTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoleTypeLocaleEntity> {

    Optional<ResortRoleTypeLocaleEntity> findByResortRoleTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoleTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByResortRoleTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoleTypeId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull ResortRoleTypeLocaleEntity> findByResortRoleTypeEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoleTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull ResortRoleTypeLocaleEntity> findByResortRoleTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoleTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select rrtl.localeEntity.code from ResortRoleTypeLocaleEntity rrtl "
            + "where rrtl.resortRoleTypeEntity.id = :resortRoleTypeId "
            + "and rrtl.isActive = :isActive and rrtl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoleTypeEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoleTypeId, Boolean isActive, Boolean isDeleted);
}
