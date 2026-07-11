package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelLocaleService;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/communication-channels/{channel-id}/locales")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @PathVariable("channel-id") Long channelId,
            @Valid @RequestBody CreateCommunicationChannelLocaleRequest request) {
        CommunicationChannelEntity channel = communicationChannelService.getEntityById(channelId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communicationChannelLocaleService.create(channel, localeEntity, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable("channel-id") Long channelId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommunicationChannelLocaleRequest request) {
        CommunicationChannelLocaleEntity entity = communicationChannelLocaleService.getEntityById(channelId, id);
        return ResponseEntity.ok(communicationChannelLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable("channel-id") Long channelId,
            @PathVariable Long id) {
        CommunicationChannelLocaleEntity entity = communicationChannelLocaleService.getEntityById(channelId, id);
        return ResponseEntity.ok(communicationChannelLocaleService.delete(entity));
    }
}
