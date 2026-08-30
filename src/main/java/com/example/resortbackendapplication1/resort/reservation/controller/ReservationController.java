package com.example.resortbackendapplication1.resort.reservation.controller;

import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.ReservationFilterRequest;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.UpdateReservationStatusRequest;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import com.example.resortbackendapplication1.resort.reservation.service.ReservationService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * No POST endpoint here — Reservation creation is deliberately not exposed on this controller. It's handled by
 * {@code BookingController} (same route, different controller class — Spring routes by method+path across every
 * controller, not per class), which resolves the customer and computes total_price before calling
 * ReservationService.create(...).
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{room-id}/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final ReservationStatusService reservationStatusService;

    public ReservationController(ReservationService reservationService,
                                 ResortRoomCategoryService resortRoomCategoryService,
                                 ResortRoomService resortRoomService,
                                 ReservationStatusService reservationStatusService) {
        this.reservationService = reservationService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.reservationStatusService = reservationStatusService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id) {
        resolveResortRoom(resortId, resortRoomCategoryId, roomId);
        return ResponseEntity.ok(reservationService.getById(roomId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @Valid @ParameterObject ReservationFilterRequest request) {
        resolveResortRoom(resortId, resortRoomCategoryId, roomId);
        return ResponseEntity.ok(reservationService.getAll(roomId, request));
    }

    /**
     * Append-only: does not update {id}'s row in place. Soft-deletes it and creates a new reservation row
     * carrying the new status — see ReservationService#transitionStatus. The returned SuccessResponse.id is
     * the NEW row's id, not the {id} path segment.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationStatusRequest request) {
        resolveResortRoom(resortId, resortRoomCategoryId, roomId);
        ReservationEntity entity = reservationService.getEntityById(roomId, id);
        ReservationStatusEntity newReservationStatusEntity = reservationStatusService.getEntityById(request.getReservationStatusId());
        return ResponseEntity.ok(reservationService.transitionStatus(entity, newReservationStatusEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @PathVariable Long id) {
        resolveResortRoom(resortId, resortRoomCategoryId, roomId);
        ReservationEntity entity = reservationService.getEntityById(roomId, id);
        return ResponseEntity.ok(reservationService.delete(entity));
    }

    private void resolveResortRoom(Long resortId, Long resortRoomCategoryId, Long roomId) {
        resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        resortRoomService.getEntityById(resortRoomCategoryId, roomId);
    }
}
