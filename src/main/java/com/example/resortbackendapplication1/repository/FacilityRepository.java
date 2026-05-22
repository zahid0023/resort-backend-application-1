package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.projection.FacilitySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacilityRepository extends JpaRepository<@NonNull FacilityEntity, @NonNull Long> {

    Optional<FacilityEntity> findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityGroupId, Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull FacilitySummary> findAllByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long facilityGroupId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    List<FacilityEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
