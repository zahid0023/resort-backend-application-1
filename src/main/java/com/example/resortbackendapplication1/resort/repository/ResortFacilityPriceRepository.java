package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortFacilityPriceRepository extends
        JpaRepository<@NonNull ResortFacilityPriceEntity, @NonNull Long> {

    Optional<ResortFacilityPriceEntity> findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortFacilityPriceEntity> findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortFacilityId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
