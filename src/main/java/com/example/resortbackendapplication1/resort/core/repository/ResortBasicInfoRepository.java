package com.example.resortbackendapplication1.resort.core.repository;

import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortBasicInfoRepository extends JpaRepository<@NonNull ResortBasicInfoEntity, @NonNull Long> {

    Optional<ResortBasicInfoEntity> findByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted);

}
