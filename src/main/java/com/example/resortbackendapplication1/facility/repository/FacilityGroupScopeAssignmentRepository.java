package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityGroupScopeAssignmentRepository extends
        JpaRepository<@NonNull FacilityGroupScopeAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityGroupScopeAssignmentEntity> {

    Optional<FacilityGroupScopeAssignmentEntity> findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityScopeEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull FacilityGroupScopeAssignmentEntity> findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityScopeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
