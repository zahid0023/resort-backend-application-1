package com.example.resortbackendapplication1.dayofweek.repository;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DayOfWeekRepository extends JpaRepository<@NonNull DayOfWeekEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull DayOfWeekEntity> {

    Optional<DayOfWeekEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<DayOfWeekEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
