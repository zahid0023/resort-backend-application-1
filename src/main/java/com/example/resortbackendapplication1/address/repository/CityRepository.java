package com.example.resortbackendapplication1.address.repository;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.projection.CitySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CityRepository extends JpaRepository<@NonNull CityEntity, @NonNull Long>,
        JpaSpecificationExecutor<CityEntity> {
    Optional<CityEntity> findByCountryEntity_IdAndIdAndIsActiveAndIsDeleted(Long countryId, Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull CitySummary> findAllByCountryEntity_IdAndIsActiveAndIsDeleted(Long countryId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
