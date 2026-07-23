package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResortFacilityRepository extends JpaRepository<@NonNull ResortFacilityEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortFacilityEntity> {

    Optional<ResortFacilityEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Optional<ResortFacilityEntity> findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(Long id, Long resortId, Boolean isActive, Boolean isDeleted);

    List<ResortFacilityEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    List<ResortFacilityEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(Long resortId, Boolean isActive, Boolean isDeleted);
}
