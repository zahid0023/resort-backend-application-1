package com.example.resortbackendapplication1.dayofweek.repository;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface DayOfWeekLocaleRepository extends
        JpaRepository<@NonNull DayOfWeekLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull DayOfWeekLocaleEntity> {

    Optional<DayOfWeekLocaleEntity> findByDayOfWeekEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long dayOfWeekId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByDayOfWeekEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long dayOfWeekId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull DayOfWeekLocaleEntity> findByDayOfWeekEntity_IdAndIsActiveAndIsDeleted(
            Long dayOfWeekId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull DayOfWeekLocaleEntity> findByDayOfWeekEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long dayOfWeekId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select dwle.localeEntity.code from DayOfWeekLocaleEntity dwle " +
            "where dwle.dayOfWeekEntity.id = :dayOfWeekId and dwle.isActive = :isActive and dwle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByDayOfWeekEntity_IdAndIsActiveAndIsDeleted(
            Long dayOfWeekId,
            Boolean isActive,
            Boolean isDeleted
    );
}
