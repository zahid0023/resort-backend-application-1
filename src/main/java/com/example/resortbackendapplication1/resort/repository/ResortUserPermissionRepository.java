package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortUserPermissionRepository extends JpaRepository<@NonNull ResortUserPermissionEntity, @NonNull Long> {

    Optional<ResortUserPermissionEntity> findByIdAndResortUserEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortUserId, Boolean isActive, Boolean isDeleted);

    Optional<ResortUserPermissionEntity> findByResortUserEntity_IdAndResortPermissionTypeEntity_Id(
            Long resortUserId, Long permissionTypeId);

    Page<ResortUserPermissionEntity> findAllByResortUserEntity_IdAndIsActiveAndIsDeleted(
            Long resortUserId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
