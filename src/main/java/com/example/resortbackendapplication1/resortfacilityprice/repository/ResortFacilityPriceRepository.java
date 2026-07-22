package com.example.resortbackendapplication1.resortfacilityprice.repository;

import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ResortFacilityPriceRepository extends
        JpaRepository<@NonNull ResortFacilityPriceEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortFacilityPriceEntity> {

    Optional<ResortFacilityPriceEntity> findByIdAndResortFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortFacilityId, Boolean isActive, Boolean isDeleted);
}
