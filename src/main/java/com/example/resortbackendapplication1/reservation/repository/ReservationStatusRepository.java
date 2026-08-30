package com.example.resortbackendapplication1.reservation.repository;

import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ReservationStatusRepository extends
        JpaRepository<@NonNull ReservationStatusEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ReservationStatusEntity> {

    Optional<ReservationStatusEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
