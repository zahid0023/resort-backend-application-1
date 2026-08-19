package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/basic-info")
public class ResortBasicInfoController {

    private final ResortBasicInfoService resortBasicInfoService;

    public ResortBasicInfoController(ResortBasicInfoService resortBasicInfoService) {
        this.resortBasicInfoService = resortBasicInfoService;
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody UpdateResortBasicInfoRequest request) {
        ResortBasicInfoEntity entity = resortBasicInfoService.getEntityByResortId(resortId);
        return ResponseEntity.ok(resortBasicInfoService.update(entity, request));
    }
}
