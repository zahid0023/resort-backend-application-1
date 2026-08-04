package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityScopeLocaleRepository extends
        JpaRepository<@NonNull FacilityScopeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityScopeLocaleEntity> {

    Optional<FacilityScopeLocaleEntity> findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
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

    Page<@NonNull FacilityScopeLocaleEntity> findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull FacilityScopeLocaleEntity> findByFacilityScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
