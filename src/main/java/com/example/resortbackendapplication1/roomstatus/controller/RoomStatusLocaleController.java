package com.example.resortbackendapplication1.roomstatus.controller;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.CreateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.UpdateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusLocaleService;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room-statuses/{room-status-id}/locales")
public class RoomStatusLocaleController {

    private final RoomStatusService roomStatusService;
    private final RoomStatusLocaleService roomStatusLocaleService;
    private final LocaleService localeService;

    public RoomStatusLocaleController(RoomStatusService roomStatusService,
                                            RoomStatusLocaleService roomStatusLocaleService,
                                            LocaleService localeService) {
        this.roomStatusService = roomStatusService;
        this.roomStatusLocaleService = roomStatusLocaleService;
        this.localeService = localeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("room-status-id") Long roomStatusId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        roomStatusService.getEntityById(roomStatusId);
        return ResponseEntity.ok(roomStatusLocaleService.getAll(roomStatusId, localeCode, paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("room-status-id") Long roomStatusId,
            @Valid @RequestBody CreateRoomStatusLocaleRequest request) {
        RoomStatusEntity roomStatusEntity = roomStatusService.getEntityById(roomStatusId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomStatusLocaleService.create(request, roomStatusEntity, localeEntity));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("room-status-id") Long roomStatusId) {
        roomStatusService.getEntityById(roomStatusId);
        return ResponseEntity.ok(roomStatusLocaleService.getCount(roomStatusId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("room-status-id") Long roomStatusId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomStatusLocaleRequest request) {
        RoomStatusLocaleEntity entity = roomStatusLocaleService.getEntityById(roomStatusId, id);
        return ResponseEntity.ok(roomStatusLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("room-status-id") Long roomStatusId,
            @PathVariable Long id) {
        RoomStatusLocaleEntity entity = roomStatusLocaleService.getEntityById(roomStatusId, id);
        return ResponseEntity.ok(roomStatusLocaleService.delete(entity));
    }
}
