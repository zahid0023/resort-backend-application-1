package com.example.resortbackendapplication1.resortbasicinfo.repository;

import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortBasicInfoLocaleRepository extends JpaRepository<@NonNull ResortBasicInfoLocaleEntity, @NonNull Long> {

    Optional<ResortBasicInfoLocaleEntity> findByResortBasicInfoEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortBasicInfoId, Long id, Boolean isActive, Boolean isDeleted);
}
