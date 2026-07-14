package com.example.resortbackendapplication1.resortpermissiontype.repository;

import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResortPermissionTypeRepository extends JpaRepository<@NonNull ResortPermissionTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortPermissionTypeEntity> {

    Optional<ResortPermissionTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Optional<ResortPermissionTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    List<ResortPermissionTypeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    List<ResortPermissionTypeEntity> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
