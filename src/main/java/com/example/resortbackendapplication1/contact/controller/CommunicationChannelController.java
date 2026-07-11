package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/communication-channels")
public class CommunicationChannelController {

    private final CommunicationChannelService communicationChannelService;
    private final LocaleService localeService;

    public CommunicationChannelController(CommunicationChannelService communicationChannelService,
                                          LocaleService localeService) {
        this.communicationChannelService = communicationChannelService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCommunicationChannelRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateCommunicationChannelLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communicationChannelService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(communicationChannelService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject CommunicationChannelFilterRequest request) {
        return ResponseEntity.ok(communicationChannelService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommunicationChannelRequest request) {
        CommunicationChannelEntity entity = communicationChannelService.getEntityById(id);
        return ResponseEntity.ok(communicationChannelService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(communicationChannelService.delete(id));
    }
}
