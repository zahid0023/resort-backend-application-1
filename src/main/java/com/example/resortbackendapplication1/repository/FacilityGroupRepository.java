package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityGroupRepository extends JpaRepository<@NonNull FacilityGroupEntity, @NonNull Long> {

    Optional<FacilityGroupEntity> findByIdAndIsActiveAndIsDeleted(Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull FacilityGroupEntity> findAllByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted, Pageable pageable);
}
