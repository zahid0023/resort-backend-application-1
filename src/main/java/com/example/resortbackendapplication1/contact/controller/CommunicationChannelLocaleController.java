package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelLocaleService;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/communication-channels/{communication-channel-id}/locales")
public class CommunicationChannelLocaleController {

    private final CommunicationChannelService communicationChannelService;
    private final CommunicationChannelLocaleService communicationChannelLocaleService;
    private final LocaleService localeService;

    public CommunicationChannelLocaleController(CommunicationChannelService communicationChannelService,
                                                CommunicationChannelLocaleService communicationChannelLocaleService,
                                                LocaleService localeService) {
        this.communicationChannelService = communicationChannelService;
        this.communicationChannelLocaleService = communicationChannelLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("communication-channel-id") Long communicationChannelId,
            @Valid @RequestBody CreateCommunicationChannelLocaleRequest request) {
        CommunicationChannelEntity communicationChannelEntity = communicationChannelService.getEntityById(communicationChannelId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communicationChannelLocaleService.create(request, communicationChannelEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("communication-channel-id") Long communicationChannelId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        communicationChannelService.getEntityById(communicationChannelId);
        return ResponseEntity.ok(communicationChannelLocaleService.getAll(communicationChannelId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("communication-channel-id") Long communicationChannelId) {
        communicationChannelService.getEntityById(communicationChannelId);
        return ResponseEntity.ok(communicationChannelLocaleService.getCount(communicationChannelId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("communication-channel-id") Long communicationChannelId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommunicationChannelLocaleRequest request) {
        CommunicationChannelLocaleEntity entity = communicationChannelLocaleService.getEntityById(communicationChannelId, id);
        return ResponseEntity.ok(communicationChannelLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("communication-channel-id") Long communicationChannelId,
            @PathVariable Long id) {
        CommunicationChannelLocaleEntity entity = communicationChannelLocaleService.getEntityById(communicationChannelId, id);
        return ResponseEntity.ok(communicationChannelLocaleService.delete(entity));
    }
}
