package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortFacilityRepository extends
        JpaRepository<@NonNull ResortFacilityEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortFacilityEntity> {

    Optional<ResortFacilityEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long facilityId, Boolean isActive, Boolean isDeleted);
}
