package com.example.resortbackendapplication1.imagehosting.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.response.ImageHostingProviderResponse;
import com.example.resortbackendapplication1.imagehosting.enums.ImageHostingProvider;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.service.ResortImageHostingConfigService;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/resort-image-hosting-configs")
public class ResortImageHostingConfigController {

    private final ResortImageHostingConfigService resortImageHostingConfigService;
    private final ResortService resortService;

    public ResortImageHostingConfigController(
            ResortImageHostingConfigService resortImageHostingConfigService,
            ResortService resortService) {
        this.resortImageHostingConfigService = resortImageHostingConfigService;
        this.resortService = resortService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortImageHostingConfigRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortImageHostingConfigService.create(request, resortEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageHostingConfigService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(resortImageHostingConfigService.getAll(resortId, request));
    }

    @GetMapping("/providers")
    public ResponseEntity<?> getProviders(@PathVariable("resort-id") Long resortId) {
        List<ImageHostingProviderResponse> providers = Arrays.stream(ImageHostingProvider.values())
                .map(ImageHostingProviderResponse::from)
                .toList();
        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortImageHostingConfigRequest request) {
        ResortImageHostingConfigEntity entity = resortImageHostingConfigService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortImageHostingConfigService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageHostingConfigService.delete(resortId, id));
    }
}
