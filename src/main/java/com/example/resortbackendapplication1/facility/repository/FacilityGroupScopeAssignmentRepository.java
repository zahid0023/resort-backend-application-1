package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityGroupScopeAssignmentRepository extends
        JpaRepository<@NonNull FacilityGroupScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityGroupScopeAssignmentEntity> {

    Optional<FacilityGroupScopeAssignmentEntity> findByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted
    );
}
