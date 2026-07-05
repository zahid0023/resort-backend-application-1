package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.auth.model.dto.CustomUserDetails;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts")
public class ResortController {

    private final ResortService resortService;

    public ResortController(ResortService resortService) {
        this.resortService = resortService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resortService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortFilterRequest request) {
        return ResponseEntity.ok(resortService.getAll(request));
    }

    @GetMapping("/my-resorts")
    public ResponseEntity<?> getAllForUser(
            @Valid @ParameterObject ResortFilterRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(resortService.getAllForUser(request, principal.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRequest request) {
        ResortEntity entity = resortService.getEntityById(id);
        return ResponseEntity.ok(resortService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.delete(id));
    }
}
