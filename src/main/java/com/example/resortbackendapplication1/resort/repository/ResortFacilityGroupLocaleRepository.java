package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortFacilityGroupLocaleRepository extends JpaRepository<@NonNull ResortFacilityGroupLocaleEntity, @NonNull Long> {

    Optional<ResortFacilityGroupLocaleEntity> findByResortFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortFacilityGroupId, Long id, Boolean isActive, Boolean isDeleted);
}
