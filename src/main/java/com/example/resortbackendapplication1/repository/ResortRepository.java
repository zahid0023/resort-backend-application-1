package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.ResortEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortRepository extends JpaRepository<@NonNull ResortEntity, @NonNull Long> {
    Optional<ResortEntity> findByIdAndIsActiveAndIsDeleted(@NonNull Long id, boolean isActive, boolean isDeleted);

    <T> Page<@NonNull T> findAllByIsActiveAndIsDeleted(
            boolean isActive,
            boolean isDeleted,
            Pageable pageable,
            Class<T> type
    );
}
