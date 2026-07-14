package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.CreateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.ResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.UpdateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resort.service.ResortUserPermissionService;
import com.example.resortbackendapplication1.resort.service.ResortUserService;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions")
public class ResortUserPermissionController {

    private final ResortUserPermissionService resortUserPermissionService;
    private final ResortUserService resortUserService;
    private final ResortPermissionTypeService resortPermissionTypeService;

    public ResortUserPermissionController(ResortUserPermissionService resortUserPermissionService,
                                          ResortUserService resortUserService,
                                          ResortPermissionTypeService resortPermissionTypeService) {
        this.resortUserPermissionService = resortUserPermissionService;
        this.resortUserService = resortUserService;
        this.resortPermissionTypeService = resortPermissionTypeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-user-id") Long resortUserId,
            @Valid @RequestBody CreateResortUserPermissionRequest request) {
        ResortUserEntity resortUserEntity = resortUserService.getEntityById(resortUserId, resortId);
        Set<Long> permissionTypeIds = request.getPermissions().stream()
                .map(ResortUserPermissionRequest::getResortPermissionTypeId)
                .collect(Collectors.toSet());
        Map<Long, ResortPermissionTypeEntity> permissionTypeEntityMap = resortPermissionTypeService.getAll(permissionTypeIds)
                .stream().collect(Collectors.toMap(ResortPermissionTypeEntity::getId, e -> e));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortUserPermissionService.create(request, resortUserEntity, permissionTypeEntityMap));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-user-id") Long resortUserId,
            @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(resortUserPermissionService.getAll(resortUserId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-user-id") Long resortUserId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortUserPermissionService.getById(id, resortUserId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-user-id") Long resortUserId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortUserPermissionRequest request) {
        ResortUserPermissionEntity entity = resortUserPermissionService.getEntityById(id, resortUserId);
        return ResponseEntity.ok(resortUserPermissionService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-user-id") Long resortUserId,
            @PathVariable Long id) {
        ResortUserPermissionEntity entity = resortUserPermissionService.getEntityById(id, resortUserId);
        return ResponseEntity.ok(resortUserPermissionService.delete(entity));
    }
}
