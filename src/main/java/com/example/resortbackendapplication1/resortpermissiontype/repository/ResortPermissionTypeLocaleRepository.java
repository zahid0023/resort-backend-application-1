package com.example.resortbackendapplication1.resortpermissiontype.repository;

import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortPermissionTypeLocaleRepository extends
        JpaRepository<@NonNull ResortPermissionTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortPermissionTypeLocaleEntity> {

    Optional<ResortPermissionTypeLocaleEntity> findByResortPermissionTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortPermissionTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByResortPermissionTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortPermissionTypeId,
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

    Page<@NonNull ResortPermissionTypeLocaleEntity> findByResortPermissionTypeEntity_IdAndIsActiveAndIsDeleted(
            Long resortPermissionTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull ResortPermissionTypeLocaleEntity> findByResortPermissionTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortPermissionTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
