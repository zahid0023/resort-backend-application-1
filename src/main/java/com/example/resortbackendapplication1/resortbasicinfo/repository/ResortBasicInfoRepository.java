package com.example.resortbackendapplication1.resortbasicinfo.repository;

import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortBasicInfoRepository extends JpaRepository<@NonNull ResortBasicInfoEntity, @NonNull Long> {

    Optional<ResortBasicInfoEntity> findByResortEntity_IdAndIsActiveAndIsDeleted(Long resortId, Boolean isActive, Boolean isDeleted);
}
