package com.example.resortbackendapplication1.resortpermissiontype.repository;

import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortPermissionTypeLocaleRepository extends JpaRepository<@NonNull ResortPermissionTypeLocaleEntity, @NonNull Long> {

    Optional<ResortPermissionTypeLocaleEntity> findByResortPermissionTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortPermissionTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
