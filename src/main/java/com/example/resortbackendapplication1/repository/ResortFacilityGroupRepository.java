package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortFacilityGroupRepository extends JpaRepository<@NonNull ResortFacilityGroupEntity, @NonNull Long> {

    Optional<ResortFacilityGroupEntity> findByIdAndIsActiveAndIsDeleted(Long id, boolean isActive, boolean isDeleted);

    Optional<ResortFacilityGroupEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(Long resortId, Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull ResortFacilityGroupEntity> findAllByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortFacilityGroupEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(Long resortId, boolean isActive, boolean isDeleted, Pageable pageable);
}
