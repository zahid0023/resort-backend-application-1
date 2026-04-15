package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortFacilityRepository extends JpaRepository<@NonNull ResortFacilityEntity, @NonNull Long> {

    Optional<ResortFacilityEntity> findByResortFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(Long resortFacilityGroupId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull ResortFacilityEntity> findAllByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(Long resortFacilityGroupId, boolean isActive, boolean isDeleted, Pageable pageable);
}
