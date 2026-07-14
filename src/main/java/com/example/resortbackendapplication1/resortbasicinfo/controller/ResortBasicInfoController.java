package com.example.resortbackendapplication1.resortbasicinfo.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.imagehosting.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.service.ImageUploadService;
import com.example.resortbackendapplication1.imagehosting.service.ResortImageHostingConfigService;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/basic-info")
public class ResortBasicInfoController {

    private final ResortBasicInfoService resortBasicInfoService;
    private final CountryService countryService;
    private final CityService cityService;
    private final ResortImageHostingConfigService imageHostingConfigService;
    private final ImageUploadService imageUploadService;

    public ResortBasicInfoController(ResortBasicInfoService resortBasicInfoService,
                                     CountryService countryService,
                                     CityService cityService,
                                     ResortImageHostingConfigService imageHostingConfigService,
                                     ImageUploadService imageUploadService) {
        this.resortBasicInfoService = resortBasicInfoService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.imageHostingConfigService = imageHostingConfigService;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping
    public ResponseEntity<?> get(@PathVariable("resort-id") Long resortId) {
        return ResponseEntity.ok(resortBasicInfoService.getByResortId(resortId));
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody UpdateResortBasicInfoRequest request) {
        ResortBasicInfoEntity entity = resortBasicInfoService.getEntityByResortId(resortId);
        CountryEntity countryEntity = countryService.getEntityById(request.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getCityId());
        return ResponseEntity.ok(resortBasicInfoService.update(entity, request, countryEntity, cityEntity));
    }

    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLogo(
            @PathVariable("resort-id") Long resortId,
            @RequestParam("config_id") Long configId,
            @RequestParam("file") MultipartFile file) {
        ResortBasicInfoEntity entity = resortBasicInfoService.getEntityByResortId(resortId);
        ResortImageHostingConfigEntity config = imageHostingConfigService.getEntityById(resortId, configId);
        ImageUploadResponse imageUploadResponse = imageUploadService.upload(file, config.getProvider(), config.getConfig());
        return ResponseEntity.ok(resortBasicInfoService.uploadLogo(entity, imageUploadResponse));
    }
}
