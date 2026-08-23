package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryRepository extends
        JpaRepository<@NonNull ResortRoomCategoryEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryEntity> {

    Optional<ResortRoomCategoryEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortId, String code, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long roomCategoryId, Boolean isActive, Boolean isDeleted);
}
