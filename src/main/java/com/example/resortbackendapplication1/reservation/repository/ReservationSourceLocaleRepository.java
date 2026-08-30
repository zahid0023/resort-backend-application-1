package com.example.resortbackendapplication1.reservation.repository;

import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ReservationSourceLocaleRepository extends
        JpaRepository<@NonNull ReservationSourceLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ReservationSourceLocaleEntity> {

    Optional<ReservationSourceLocaleEntity> findByReservationSourceEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long reservationSourceId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByReservationSourceEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long reservationSourceId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull ReservationSourceLocaleEntity> findByReservationSourceEntity_IdAndIsActiveAndIsDeleted(
            Long reservationSourceId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull ReservationSourceLocaleEntity> findByReservationSourceEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long reservationSourceId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select rsl.localeEntity.code from ReservationSourceLocaleEntity rsl " +
            "where rsl.reservationSourceEntity.id = :reservationSourceId and rsl.isActive = :isActive and rsl.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByReservationSourceEntity_IdAndIsActiveAndIsDeleted(
            Long reservationSourceId,
            Boolean isActive,
            Boolean isDeleted
    );
}
