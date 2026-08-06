package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupFacilityAssignmentEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FacilityGroupFacilityAssignmentRepository extends
        JpaRepository<@NonNull FacilityGroupFacilityAssignmentEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityGroupFacilityAssignmentEntity> {

    Optional<FacilityGroupFacilityAssignmentEntity> findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByFacilityGroupEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Long facilityId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull FacilityGroupFacilityAssignmentEntity> findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
