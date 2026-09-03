package com.example.resortbackendapplication1.mail.provider.controller;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.CreateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.UpdateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigFieldEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigFieldService;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail-providers/{mail-provider-id}/config-fields")
public class MailProviderConfigFieldController {

    private final MailProviderService mailProviderService;
    private final MailProviderConfigFieldService mailProviderConfigFieldService;

    public MailProviderConfigFieldController(
            MailProviderService mailProviderService,
            MailProviderConfigFieldService mailProviderConfigFieldService) {
        this.mailProviderService = mailProviderService;
        this.mailProviderConfigFieldService = mailProviderConfigFieldService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @Valid @RequestBody CreateMailProviderConfigFieldRequest request) {
        MailProviderEntity providerEntity = mailProviderService.getEntityById(mailProviderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mailProviderConfigFieldService.create(request, providerEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable("mail-provider-id") Long mailProviderId) {
        mailProviderService.getEntityById(mailProviderId);
        return ResponseEntity.ok(mailProviderConfigFieldService.getAll(mailProviderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateMailProviderConfigFieldRequest request) {
        MailProviderConfigFieldEntity entity = mailProviderConfigFieldService.getEntityById(mailProviderId, id);
        return ResponseEntity.ok(mailProviderConfigFieldService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("mail-provider-id") Long mailProviderId,
            @PathVariable Long id) {
        MailProviderConfigFieldEntity entity = mailProviderConfigFieldService.getEntityById(mailProviderId, id);
        return ResponseEntity.ok(mailProviderConfigFieldService.delete(entity));
    }
}
