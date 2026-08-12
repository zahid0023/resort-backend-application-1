package com.example.resortbackendapplication1.resortpermissiontype.repository;

import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortPermissionTypeRepository extends
        JpaRepository<@NonNull ResortPermissionTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortPermissionTypeEntity> {

    Optional<ResortPermissionTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
