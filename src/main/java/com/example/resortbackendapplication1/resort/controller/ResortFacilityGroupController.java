package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/facility-groups")
public class ResortFacilityGroupController {

    private final ResortService resortService;
    private final ResortFacilityGroupService resortFacilityGroupService;
    private final FacilityGroupService facilityGroupService;
    private final LocaleService localeService;

    public ResortFacilityGroupController(ResortService resortService,
                                         ResortFacilityGroupService resortFacilityGroupService,
                                         FacilityGroupService facilityGroupService,
                                         LocaleService localeService) {
        this.resortService = resortService;
        this.resortFacilityGroupService = resortFacilityGroupService;
        this.facilityGroupService = facilityGroupService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortFacilityGroupRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        FacilityGroupEntity facilityGroupEntity = request.getFacilityGroupId() != null
                ? facilityGroupService.getEntityById(request.getFacilityGroupId())
                : null;
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateResortFacilityGroupLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortFacilityGroupService.create(request, resortEntity, facilityGroupEntity, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityGroupService.getById(id, resortId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortFacilityGroupFilterRequest request) {
        return ResponseEntity.ok(resortFacilityGroupService.getAll(request, resortId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortFacilityGroupRequest request) {
        ResortFacilityGroupEntity entity = resortFacilityGroupService.getEntityById(id, resortId);
        FacilityGroupEntity facilityGroupEntity = request.getFacilityGroupId() != null
                ? facilityGroupService.getEntityById(request.getFacilityGroupId())
                : null;
        return ResponseEntity.ok(resortFacilityGroupService.update(entity, request, facilityGroupEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortFacilityGroupService.delete(id, resortId));
    }
}
