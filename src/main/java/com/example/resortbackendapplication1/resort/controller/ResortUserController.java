package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuser.CreateResortUserRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuser.UpdateResortUserRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.service.ResortUserService;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/users")
public class ResortUserController {

    private final ResortUserService resortUserService;
    private final ResortService resortService;
    private final UserService userService;
    private final ResortAccessTypeService resortAccessTypeService;

    public ResortUserController(ResortUserService resortUserService,
                                ResortService resortService,
                                UserService userService,
                                ResortAccessTypeService resortAccessTypeService) {
        this.resortUserService = resortUserService;
        this.resortService = resortService;
        this.userService = userService;
        this.resortAccessTypeService = resortAccessTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortUserRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        UserEntity userEntity = userService.getUserById(request.getUserId());
        ResortAccessTypeEntity accessTypeEntity = resortAccessTypeService.getEntityById(request.getResortAccessTypeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortUserService.create(resortEntity, userEntity, accessTypeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(resortUserService.getAll(resortId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortUserService.getById(id, resortId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortUserRequest request) {
        ResortUserEntity entity = resortUserService.getEntityById(id, resortId);
        ResortAccessTypeEntity accessTypeEntity = resortAccessTypeService.getEntityById(request.getResortAccessTypeId());
        return ResponseEntity.ok(resortUserService.update(entity, accessTypeEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortUserEntity entity = resortUserService.getEntityById(id, resortId);
        return ResponseEntity.ok(resortUserService.delete(entity));
    }
}
