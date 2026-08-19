package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities")
public class ResortFacilityController {

    private final ResortFacilityService resortFacilityService;
    private final ResortService resortService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final FacilityService facilityService;
    private final LocaleService localeService;

    public ResortFacilityController(ResortFacilityService resortFacilityService,
                                    ResortService resortService,
                                    ResortFacilityGroupService resortFacilityGroupService,
                                    FacilityService facilityService,
                                    LocaleService localeService) {
        this.resortFacilityService = resortFacilityService;
        this.resortService = resortService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.facilityService = facilityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        ResortFacilityGroupEntity resortFacilityGroupEntity =
                resortFacilityGroupService.getEntityById(resortId, request.getResortFacilityGroupId());
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity, localeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortFacilityService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(resortId, id);
        ResortFacilityGroupEntity resortFacilityGroupEntity =
                resortFacilityGroupService.getEntityById(resortId, request.getResortFacilityGroupId());
        return ResponseEntity.ok(resortFacilityService.update(entity, resortFacilityGroupEntity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortFacilityService.delete(entity));
    }
}
