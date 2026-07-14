package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.ImageMetaRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.ImageRequest;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.service.ImageUploadService;
import com.example.resortbackendapplication1.imagehosting.service.ResortImageHostingConfigService;
import com.example.resortbackendapplication1.resort.dto.request.resortimage.UpdateResortImageRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortImageEntity;
import com.example.resortbackendapplication1.resort.service.ResortImageService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/images")
public class ResortImageController {

    private final ResortImageService resortImageService;
    private final ResortImageHostingConfigService imageHostingConfigService;
    private final ImageUploadService imageUploadService;

    public ResortImageController(ResortImageService resortImageService,
                                 ResortImageHostingConfigService imageHostingConfigService,
                                 ImageUploadService imageUploadService) {
        this.resortImageService = resortImageService;
        this.imageHostingConfigService = imageHostingConfigService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("resort-id") Long resortId,
            @RequestPart("meta") List<ImageMetaRequest> imageMetaRequests,
            @RequestParam("config_id") Long configId,
            @RequestPart("images") List<MultipartFile> images) {
        ResortImageHostingConfigEntity config = imageHostingConfigService.getEntityById(resortId, configId);
        List<ImageRequest> imageRequests = imageUploadService.uploadAll(
                images, imageMetaRequests, config.getProvider(), config.getConfig()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortImageService.createAll(imageRequests, config));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(resortImageService.getAll(resortId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortImageService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortImageRequest request) {
        ResortImageEntity entity = resortImageService.getEntityById(id);
        return ResponseEntity.ok(resortImageService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortImageEntity entity = resortImageService.getEntityById(id);
        return ResponseEntity.ok(resortImageService.delete(entity));
    }
}
