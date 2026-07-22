package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityGroupScopeAssignmentRepository
        extends JpaRepository<@NonNull FacilityGroupScopeAssignmentEntity, @NonNull FacilityGroupScopeAssignmentId> {

    List<FacilityGroupScopeAssignmentEntity> findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId, Boolean isActive, Boolean isDeleted);

    Optional<FacilityGroupScopeAssignmentEntity> findByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId, Long facilityScopeId, Boolean isActive, Boolean isDeleted);
}
