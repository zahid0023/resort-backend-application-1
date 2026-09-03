package com.example.resortbackendapplication1.resort.mail.controller;

import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.CreateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigFilterRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.UpdateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;
import com.example.resortbackendapplication1.resort.mail.service.ResortMailConfigService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/mail-configs")
public class ResortMailConfigController {

    private final ResortMailConfigService resortMailConfigService;
    private final ResortService resortService;
    private final MailProviderService mailProviderService;

    public ResortMailConfigController(ResortMailConfigService resortMailConfigService,
                                      ResortService resortService,
                                      MailProviderService mailProviderService) {
        this.resortMailConfigService = resortMailConfigService;
        this.resortService = resortService;
        this.mailProviderService = mailProviderService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortMailConfigRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        MailProviderEntity mailProviderEntity = mailProviderService.getEntityById(request.getMailProviderId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortMailConfigService.create(request, resortEntity, mailProviderEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortMailConfigService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortMailConfigFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortMailConfigService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortMailConfigRequest request) {
        ResortMailConfigEntity entity = resortMailConfigService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortMailConfigService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortMailConfigEntity entity = resortMailConfigService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortMailConfigService.delete(entity));
    }
}
