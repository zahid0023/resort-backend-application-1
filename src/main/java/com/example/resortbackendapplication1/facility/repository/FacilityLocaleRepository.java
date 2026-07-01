package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityLocaleRepository extends JpaRepository<@NonNull FacilityLocaleEntity, @NonNull Long> {
    Optional<FacilityLocaleEntity> findByFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityId, Long id, Boolean isActive, Boolean isDeleted);
}
