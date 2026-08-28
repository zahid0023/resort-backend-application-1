package com.example.resortbackendapplication1.roomstatus.controller;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusFilterRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.CreateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.UpdateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room-statuses")
public class RoomStatusController {

    private final RoomStatusService roomStatusService;
    private final LocaleService localeService;

    public RoomStatusController(RoomStatusService roomStatusService,
                                      LocaleService localeService) {
        this.roomStatusService = roomStatusService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRoomStatusRequest request) {
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED).body(roomStatusService.create(request, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomStatusService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject RoomStatusFilterRequest request) {
        return ResponseEntity.ok(roomStatusService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomStatusRequest request) {
        RoomStatusEntity entity = roomStatusService.getEntityById(id);
        return ResponseEntity.ok(roomStatusService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        RoomStatusEntity entity = roomStatusService.getEntityById(id);
        return ResponseEntity.ok(roomStatusService.delete(entity));
    }
}
