package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortFacilityLocaleRepository extends JpaRepository<@NonNull ResortFacilityLocaleEntity, @NonNull Long> {

    Optional<ResortFacilityLocaleEntity> findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Long id, Boolean isActive, Boolean isDeleted);
}
