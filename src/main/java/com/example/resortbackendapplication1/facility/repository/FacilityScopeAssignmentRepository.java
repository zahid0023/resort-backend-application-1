package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityScopeAssignmentRepository
        extends JpaRepository<@NonNull FacilityScopeAssignmentEntity, @NonNull FacilityScopeAssignmentId> {

    List<FacilityScopeAssignmentEntity> findByFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId, Boolean isActive, Boolean isDeleted);

    Optional<FacilityScopeAssignmentEntity> findByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
            Long facilityId, Long facilityScopeId, Boolean isActive, Boolean isDeleted);
}
