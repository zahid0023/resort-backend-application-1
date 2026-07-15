package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResortFacilityGroupRepository extends JpaRepository<@NonNull ResortFacilityGroupEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortFacilityGroupEntity> {

    Optional<ResortFacilityGroupEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<ResortFacilityGroupEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndFacilityGroupEntity_IdAndIsDeleted(Long resortId, Long facilityGroupId, Boolean isDeleted);

    boolean existsByResortEntity_IdAndFacilityGroupEntity_IdAndIdNotAndIsDeleted(Long resortId, Long facilityGroupId, Long excludeId, Boolean isDeleted);
}
