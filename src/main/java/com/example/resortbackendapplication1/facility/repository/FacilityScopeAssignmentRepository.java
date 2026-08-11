package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityScopeAssignmentRepository extends
        JpaRepository<@NonNull FacilityScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityScopeAssignmentEntity> {

    Optional<FacilityScopeAssignmentEntity> findByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
