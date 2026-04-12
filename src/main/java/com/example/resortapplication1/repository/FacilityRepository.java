package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.FacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<@NonNull FacilityEntity, @NonNull Long> {

    Optional<FacilityEntity> findByIdAndIsActiveAndIsDeleted(Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull FacilityEntity> findAllByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted, Pageable pageable);
}
