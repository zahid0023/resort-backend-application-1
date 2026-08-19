package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityOperatingHoursEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@SuppressWarnings("unused")
public interface ResortFacilityOperatingHoursRepository extends
        JpaRepository<@NonNull ResortFacilityOperatingHoursEntity, @NonNull Long> {

    Page<@NonNull ResortFacilityOperatingHoursEntity> findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    List<ResortFacilityOperatingHoursEntity> findAllByResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Boolean isActive, Boolean isDeleted);
}
