package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
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

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facilities")
public class ResortFacilityController {

    private final ResortService resortService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final ResortFacilityService resortFacilityService;
    private final FacilityService facilityService;
    private final LocaleService localeService;

    public ResortFacilityController(ResortService resortService,
                                    ResortFacilityGroupService resortFacilityGroupService,
                                    ResortFacilityService resortFacilityService,
                                    FacilityService facilityService,
                                    LocaleService localeService) {
        this.resortService = resortService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.resortFacilityService = resortFacilityService;
        this.facilityService = facilityService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        ResortFacilityGroupEntity resortFacilityGroupEntity = resortFacilityGroupService.getEntityById(request.getResortFacilityGroupId());
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortFacilityLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityService.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityFilterRequest request,
            @RequestParam(required = false) Long resortFacilityGroupId) {
        return ResponseEntity.ok(resortFacilityService.getAll(request, resortFacilityGroupId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityRequest request) {
        ResortFacilityEntity entity = resortFacilityService.getEntityById(id);
        FacilityEntity facilityEntity = request.getFacilityId() != null
                ? facilityService.getEntityById(request.getFacilityId())
                : null;
        return ResponseEntity.ok(resortFacilityService.update(entity, request, facilityEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityService.delete(id));
    }
}
