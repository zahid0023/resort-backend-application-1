package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.ImageMetaRequest;
import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.service.ImageUploadService;
import com.example.resortbackendapplication1.dto.response.resortimagestorageconfigs.ResortImageStorageConfigResponse;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.service.ResortImageService;
import com.example.resortbackendapplication1.service.ResortImageStorageConfigService;
import com.example.resortbackendapplication1.service.ResortService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/images")
public class ResortImageController {

    private final ResortImageService resortImageService;
    private final ResortService resortService;
    private final ResortImageStorageConfigService resortImageStorageConfigService;
    private final ImageUploadService imageUploadService;

    public ResortImageController(ResortImageService resortImageService,
                                 ResortService resortService,
                                 ResortImageStorageConfigService resortImageStorageConfigService,
                                 ImageUploadService imageUploadService) {
        this.resortImageService = resortImageService;
        this.resortService = resortService;
        this.resortImageStorageConfigService = resortImageStorageConfigService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("resort-id") Long resortId,
            @ModelAttribute List<ImageMetaRequest> request) {
        ResortEntity resortEntity = resortService.getResortById(resortId);

        ResortImageStorageConfigResponse resortImageStorageConfigResponse = resortImageStorageConfigService.getResortImageStorageConfig(resortId);

        Map<String, String> config = resortImageStorageConfigResponse.getData().getConfig();

        List<ImageRequest> imageRequests = List.of(null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortImageService.saveImages(resortEntity, imageRequests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResortImage(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageService.getResortImage(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAllResortImages(
            @PathVariable("resort-id") Long resortId,
            @ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "sortOrder", "isDefault"));
        return ResponseEntity.ok(resortImageService.getAllResortImages(resortId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResortImage(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @RequestBody ImageRequest imageRequest) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResortImage(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageService.deleteResortImage(resortId, id));
    }
}
