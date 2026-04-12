package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.CountryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<@NonNull CountryEntity, @NonNull Long> {
    Optional<CountryEntity> findByIdAndIsActiveAndIsDeleted(@NonNull Long id, boolean isActive, boolean isDeleted);

    <T> Page<@NonNull T> findAllByIsActiveAndIsDeleted(
            boolean isActive,
            boolean isDeleted,
            Pageable pageable,
            Class<T> type
    );
}
