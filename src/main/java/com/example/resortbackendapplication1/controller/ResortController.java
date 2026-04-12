package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.service.ResortService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts")
public class ResortController {

    private final ResortService resortService;

    public ResortController(ResortService resortService) {
        this.resortService = resortService;
    }

    @PostMapping
    public ResponseEntity<?> createResort(@RequestBody CreateResortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resortService.createResort(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResort(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.getResort(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResorts(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "name"));
        return ResponseEntity.ok(resortService.getAllResorts(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResort(@PathVariable Long id, @RequestBody UpdateResortRequest request) {
        return ResponseEntity.ok(resortService.updateResort(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResort(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.deleteResort(id));
    }
}
