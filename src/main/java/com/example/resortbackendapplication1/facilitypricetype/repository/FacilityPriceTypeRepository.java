package com.example.resortbackendapplication1.facilitypricetype.repository;

import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FacilityPriceTypeRepository extends JpaRepository<@NonNull FacilityPriceTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityPriceTypeEntity> {

    Optional<FacilityPriceTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);
}
