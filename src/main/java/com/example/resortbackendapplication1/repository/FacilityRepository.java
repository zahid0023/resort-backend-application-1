package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacilityRepository extends JpaRepository<@NonNull FacilityEntity, @NonNull Long> {

    Optional<FacilityEntity> findByIdAndIsActiveAndIsDeleted(Long id, boolean isActive, boolean isDeleted);

    Page<@NonNull FacilityEntity> findAllByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted, Pageable pageable);

    List<FacilityEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
