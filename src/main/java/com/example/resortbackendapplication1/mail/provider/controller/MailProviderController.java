package com.example.resortbackendapplication1.mail.provider.controller;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.CreateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.UpdateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail-providers")
public class MailProviderController {

    private final MailProviderService mailProviderService;

    public MailProviderController(MailProviderService mailProviderService) {
        this.mailProviderService = mailProviderService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateMailProviderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mailProviderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mailProviderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject MailProviderFilterRequest request) {
        return ResponseEntity.ok(mailProviderService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMailProviderRequest request) {
        MailProviderEntity entity = mailProviderService.getEntityById(id);
        return ResponseEntity.ok(mailProviderService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        MailProviderEntity entity = mailProviderService.getEntityById(id);
        return ResponseEntity.ok(mailProviderService.delete(entity));
    }
}
