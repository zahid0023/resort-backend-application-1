package com.example.resortbackendapplication1.reservation.repository;

import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ReservationStatusLocaleRepository extends
        JpaRepository<@NonNull ReservationStatusLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ReservationStatusLocaleEntity> {

    Optional<ReservationStatusLocaleEntity> findByReservationStatusEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long reservationStatusId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByReservationStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long reservationStatusId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull ReservationStatusLocaleEntity> findByReservationStatusEntity_IdAndIsActiveAndIsDeleted(
            Long reservationStatusId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull ReservationStatusLocaleEntity> findByReservationStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long reservationStatusId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select rsl.localeEntity.code from ReservationStatusLocaleEntity rsl " +
            "where rsl.reservationStatusEntity.id = :reservationStatusId and rsl.isActive = :isActive and rsl.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByReservationStatusEntity_IdAndIsActiveAndIsDeleted(
            Long reservationStatusId,
            Boolean isActive,
            Boolean isDeleted
    );
}
