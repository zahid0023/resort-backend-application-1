package com.example.resortbackendapplication1.resortaccesstype.repository;

import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortAccessTypeLocaleRepository extends JpaRepository<@NonNull ResortAccessTypeLocaleEntity, @NonNull Long> {

    Optional<ResortAccessTypeLocaleEntity> findByResortAccessTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortAccessTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
