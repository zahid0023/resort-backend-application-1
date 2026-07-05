package com.example.resortbackendapplication1.imagehosting.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.response.ImageHostingProviderResponse;
import com.example.resortbackendapplication1.imagehosting.enums.ImageHostingProvider;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.service.ResortImageHostingConfigService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resort-image-hosting-configs")
public class ResortImageHostingConfigController {

    private final ResortImageHostingConfigService resortImageHostingConfigService;

    public ResortImageHostingConfigController(
            ResortImageHostingConfigService resortImageHostingConfigService) {
        this.resortImageHostingConfigService = resortImageHostingConfigService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortImageHostingConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortImageHostingConfigService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortImageHostingConfigService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(resortImageHostingConfigService.getAll(request));
    }

    @GetMapping("/providers")
    public ResponseEntity<?> getProviders() {
        List<ImageHostingProviderResponse> providers = Arrays.stream(ImageHostingProvider.values())
                .map(ImageHostingProviderResponse::from)
                .toList();
        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortImageHostingConfigRequest request) {
        ResortImageHostingConfigEntity entity = resortImageHostingConfigService.getEntityById(id);
        return ResponseEntity.ok(resortImageHostingConfigService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(resortImageHostingConfigService.delete(id));
    }
}
