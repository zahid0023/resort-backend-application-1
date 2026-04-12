package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.CityEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<@NonNull CityEntity, @NonNull Long> {
    Optional<CityEntity> findByIdAndIsActiveAndIsDeleted(@NonNull Long id, boolean isActive, boolean isDeleted);

    <T> Page<@NonNull T> findAllByIsActiveAndIsDeleted(
            boolean isActive,
            boolean isDeleted,
            Pageable pageable,
            Class<T> type
    );
}
