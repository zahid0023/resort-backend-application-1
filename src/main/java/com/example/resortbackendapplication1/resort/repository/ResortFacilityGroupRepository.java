package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortFacilityGroupRepository extends
        JpaRepository<@NonNull ResortFacilityGroupEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortFacilityGroupEntity> {

    Optional<ResortFacilityGroupEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long facilityGroupId, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortId, String code, Boolean isActive, Boolean isDeleted);
}
