package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.ResortAccessTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortAccessTypeRepository extends JpaRepository<@NonNull ResortAccessTypeEntity, @NonNull Long> {
    Optional<ResortAccessTypeEntity> findByIdAndIsActiveAndIsDeleted(@NonNull Long id, boolean isActive, boolean isDeleted);

    Optional<ResortAccessTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, boolean isActive, boolean isDeleted);

    <T> Page<@NonNull T> findAllByIsActiveAndIsDeleted(
            boolean isActive,
            boolean isDeleted,
            Pageable pageable,
            Class<T> type
    );
}
