package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.UpdateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageStorageConfigEntity;
import com.example.resortbackendapplication1.service.ResortImageStorageConfigService;
import com.example.resortbackendapplication1.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-image-storage-configs")
public class ResortImageStorageConfigController {

    private final ResortImageStorageConfigService resortImageStorageConfigService;
    private final ResortService resortService;

    public ResortImageStorageConfigController(ResortImageStorageConfigService resortImageStorageConfigService,
                                              ResortService resortService) {
        this.resortImageStorageConfigService = resortImageStorageConfigService;
        this.resortService = resortService;
    }

    @PostMapping
    public ResponseEntity<?> createResortImageStorageConfig(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortImageStorageConfigRequest request) {
        ResortEntity resortEntity = resortService.getResortById(resortId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortImageStorageConfigService.createResortImageStorageConfig(request, resortEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortImageStorageConfig(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageStorageConfigService.getResortImageStorageConfig(resortId, id));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveResortImageStorageConfig(
            @PathVariable("resort-id") Long resortId) {
        return ResponseEntity.ok(resortImageStorageConfigService.getResortImageStorageConfig(resortId));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortImageStorageConfigs(
            @PathVariable("resort-id") Long resortId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "provider"));
        return ResponseEntity.ok(resortImageStorageConfigService.getAllResortImageStorageConfigs(resortId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortImageStorageConfig(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortImageStorageConfigRequest request) {
        ResortImageStorageConfigEntity entity = resortImageStorageConfigService.getResortImageStorageConfigEntity(resortId, id);
        return ResponseEntity.ok(resortImageStorageConfigService.updateResortImageStorageConfig(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortImageStorageConfig(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageStorageConfigService.deleteResortImageStorageConfig(resortId, id));
    }
}
