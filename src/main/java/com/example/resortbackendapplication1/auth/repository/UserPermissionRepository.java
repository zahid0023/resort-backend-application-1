package com.example.resortbackendapplication1.auth.repository;

import com.example.resortbackendapplication1.auth.model.entity.PermissionEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserPermissionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@SuppressWarnings("unused")
public interface UserPermissionRepository extends
        JpaRepository<@NonNull UserPermissionEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull UserPermissionEntity> {

    boolean existsByUserEntityAndPermissionEntity(
            UserEntity userEntity,
            PermissionEntity permissionEntity
    );

}
