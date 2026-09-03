package com.example.resortbackendapplication1.mail.provider.controller;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.CreateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.UpdateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigService;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail-providers/{mail-provider-id}/configs")
public class MailProviderConfigController {

    private final MailProviderConfigService mailProviderConfigService;
    private final MailProviderService mailProviderService;

    public MailProviderConfigController(MailProviderConfigService mailProviderConfigService,
                                         MailProviderService mailProviderService) {
        this.mailProviderConfigService = mailProviderConfigService;
        this.mailProviderService = mailProviderService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @Valid @RequestBody CreateMailProviderConfigRequest request) {
        MailProviderEntity providerEntity = mailProviderService.getEntityById(mailProviderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mailProviderConfigService.create(request, providerEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @Valid @ParameterObject MailProviderConfigFilterRequest request) {
        mailProviderService.getEntityById(mailProviderId);
        request.setMailProviderId(mailProviderId);
        return ResponseEntity.ok(mailProviderConfigService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateMailProviderConfigRequest request) {
        MailProviderConfigEntity entity = mailProviderConfigService.getEntityById(mailProviderId, id);
        return ResponseEntity.ok(mailProviderConfigService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @PathVariable Long id) {
        MailProviderConfigEntity entity = mailProviderConfigService.getEntityById(mailProviderId, id);
        return ResponseEntity.ok(mailProviderConfigService.delete(entity));
    }
}
