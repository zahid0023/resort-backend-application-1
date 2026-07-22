package com.example.resortbackendapplication1.facilitypricetype.repository;

import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityPriceTypeLocaleRepository extends JpaRepository<@NonNull FacilityPriceTypeLocaleEntity, @NonNull Long> {

    Optional<FacilityPriceTypeLocaleEntity> findByFacilityPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long facilityPriceTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
