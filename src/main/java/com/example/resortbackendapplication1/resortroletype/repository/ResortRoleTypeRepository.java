package com.example.resortbackendapplication1.resortroletype.repository;

import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoleTypeRepository extends
        JpaRepository<@NonNull ResortRoleTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoleTypeEntity> {

    Optional<ResortRoleTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Optional<ResortRoleTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
