package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacilityGroupRepository extends JpaRepository<@NonNull FacilityGroupEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityGroupEntity> {

    Optional<FacilityGroupEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<FacilityGroupEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
