package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityScopeAssignmentRepository extends
        JpaRepository<@NonNull FacilityScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityScopeAssignmentEntity> {

    Optional<FacilityScopeAssignmentEntity> findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityScopeEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Long facilityId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull FacilityScopeAssignmentEntity> findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
