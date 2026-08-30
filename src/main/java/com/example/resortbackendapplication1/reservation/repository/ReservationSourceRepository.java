package com.example.resortbackendapplication1.reservation.repository;

import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ReservationSourceRepository extends
        JpaRepository<@NonNull ReservationSourceEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ReservationSourceEntity> {

    Optional<ReservationSourceEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
