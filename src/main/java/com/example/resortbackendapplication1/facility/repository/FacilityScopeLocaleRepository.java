package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityScopeLocaleRepository extends JpaRepository<@NonNull FacilityScopeLocaleEntity, @NonNull Long> {

    Optional<FacilityScopeLocaleEntity> findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityScopeId, Long id, Boolean isActive, Boolean isDeleted);
}
