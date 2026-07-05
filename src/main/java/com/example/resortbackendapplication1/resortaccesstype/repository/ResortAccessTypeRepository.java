package com.example.resortbackendapplication1.resortaccesstype.repository;

import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResortAccessTypeRepository extends JpaRepository<@NonNull ResortAccessTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortAccessTypeEntity> {

    Optional<ResortAccessTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<ResortAccessTypeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    Optional<ResortAccessTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
