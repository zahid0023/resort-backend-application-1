package com.example.resortbackendapplication1.dayofweek.repository;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DayOfWeekLocaleRepository extends JpaRepository<@NonNull DayOfWeekLocaleEntity, @NonNull Long> {

    Optional<DayOfWeekLocaleEntity> findByDayOfWeekEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long dayOfWeekId, Long id, Boolean isActive, Boolean isDeleted);
}
