package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.CityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityLocaleRepository extends JpaRepository<@NonNull CityLocaleEntity, @NonNull Long> {
    Optional<CityLocaleEntity> findByCityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long cityId, Long id, Boolean isActive, Boolean isDeleted);
}
