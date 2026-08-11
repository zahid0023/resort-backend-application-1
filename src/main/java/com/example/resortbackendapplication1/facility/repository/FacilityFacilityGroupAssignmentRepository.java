package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityFacilityGroupAssignmentRepository extends
        JpaRepository<@NonNull FacilityFacilityGroupAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityFacilityGroupAssignmentEntity> {

    Optional<FacilityFacilityGroupAssignmentEntity> findByFacilityEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId,
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted
    );
}
