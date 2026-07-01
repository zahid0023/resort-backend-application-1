package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityGroupLocaleRepository extends JpaRepository<@NonNull FacilityGroupLocaleEntity, @NonNull Long> {
    Optional<FacilityGroupLocaleEntity> findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityGroupId, Long id, Boolean isActive, Boolean isDeleted);
}
