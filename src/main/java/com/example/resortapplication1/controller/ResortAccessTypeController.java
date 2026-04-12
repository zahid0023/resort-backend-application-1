package com.example.resortapplication1.controller;

import com.example.resortapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortapplication1.dto.request.resortaccesstypes.CreateResortAccessTypeRequest;
import com.example.resortapplication1.dto.request.resortaccesstypes.UpdateResortAccessTypeRequest;
import com.example.resortapplication1.service.ResortAccessTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/resort-access-types")
public class ResortAccessTypeController {

    private final ResortAccessTypeService resortAccessTypeService;

    public ResortAccessTypeController(ResortAccessTypeService resortAccessTypeService) {
        this.resortAccessTypeService = resortAccessTypeService;
    }

    @PostMapping
    public ResponseEntity<?> createResortAccessType(@RequestBody CreateResortAccessTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resortAccessTypeService.createResortAccessType(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortAccessType(@PathVariable Long id) {
        return ResponseEntity.ok(resortAccessTypeService.getResortAccessType(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortAccessTypes(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(resortAccessTypeService.getAllResortAccessTypes(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortAccessType(@PathVariable Long id, @RequestBody UpdateResortAccessTypeRequest request) {
        return ResponseEntity.ok(resortAccessTypeService.updateResortAccessType(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortAccessType(@PathVariable Long id) {
        return ResponseEntity.ok(resortAccessTypeService.deleteResortAccessType(id));
    }
}
