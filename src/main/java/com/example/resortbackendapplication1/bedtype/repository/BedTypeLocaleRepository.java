package com.example.resortbackendapplication1.bedtype.repository;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BedTypeLocaleRepository extends JpaRepository<@NonNull BedTypeLocaleEntity, @NonNull Long> {

    Optional<BedTypeLocaleEntity> findByBedTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long bedTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
