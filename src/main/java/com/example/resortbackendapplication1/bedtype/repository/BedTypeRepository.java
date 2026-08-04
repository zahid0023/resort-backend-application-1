package com.example.resortbackendapplication1.bedtype.repository;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface BedTypeRepository extends
        JpaRepository<@NonNull BedTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull BedTypeEntity> {

    Optional<BedTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
