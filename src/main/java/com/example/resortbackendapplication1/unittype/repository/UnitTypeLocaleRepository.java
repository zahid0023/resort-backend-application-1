package com.example.resortbackendapplication1.unittype.repository;

import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitTypeLocaleRepository extends JpaRepository<@NonNull UnitTypeLocaleEntity, @NonNull Long> {

    Optional<UnitTypeLocaleEntity> findByUnitTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long unitTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
