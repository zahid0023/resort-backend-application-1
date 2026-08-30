package com.example.resortbackendapplication1.resort.reservation.repository;

import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public interface ReservationRepository extends JpaRepository<@NonNull ReservationEntity, @NonNull Long> {

    /**
     * Backs AvailabilityService — of the given rooms, which ones already have an active reservation
     * overlapping [checkIn, checkOut). Mirrors the half-open-interval overlap test the DB's own
     * excl_reservations_no_overlap exclusion constraint (V45) enforces at insert time: existing.checkIn <
     * newCheckOut and existing.checkOut > newCheckIn.
     */
    @Query("select distinct r.resortRoomEntity.id from ReservationEntity r "
            + "where r.resortRoomEntity.id in :resortRoomIds "
            + "and r.blocksAvailability = true and r.isDeleted = false "
            + "and r.checkIn < :checkOut and r.checkOut > :checkIn")
    Set<Long> findOccupiedResortRoomIds(@Param("resortRoomIds") Collection<Long> resortRoomIds,
                                        @Param("checkIn") LocalDate checkIn,
                                        @Param("checkOut") LocalDate checkOut);

    Optional<ReservationEntity> findByIdAndResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortRoomId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ReservationEntity> findByUserEntity_IdAndIsActiveAndIsDeleted(
            Long customerId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ReservationEntity> findByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ReservationEntity> findByResortRoomEntity_ResortRoomCategoryEntity_ResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
