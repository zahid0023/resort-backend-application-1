package com.example.resortbackendapplication1.resort.roomreservation.repository;

import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@SuppressWarnings("unused")
public interface ResortRoomReservationRepository extends JpaRepository<@NonNull ResortRoomReservationEntity, @NonNull Long> {

    /**
     * Backs AvailabilityService — of the given rooms, which ones already have an active reservation
     * overlapping [checkIn, checkOut). Mirrors the half-open-interval overlap test the DB's own
     * excl_resort_room_reservations_no_overlap exclusion constraint (V46) enforces at insert time: existing.checkIn <
     * newCheckOut and existing.checkOut > newCheckIn.
     */
    @Query("select distinct r.resortRoomEntity.id from ResortRoomReservationEntity r "
            + "where r.resortRoomEntity.id in :resortRoomIds "
            + "and r.blocksAvailability = true and r.isDeleted = false "
            + "and r.checkIn < :checkOut and r.checkOut > :checkIn")
    Set<Long> findOccupiedResortRoomIds(@Param("resortRoomIds") Collection<Long> resortRoomIds,
                                        @Param("checkIn") LocalDate checkIn,
                                        @Param("checkOut") LocalDate checkOut);
}
