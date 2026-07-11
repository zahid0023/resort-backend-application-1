package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.contact.service.ContactTypeLocaleService;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact-types/{contact-type-id}/locales")
public class ContactTypeLocaleController {

    private final ContactTypeService contactTypeService;
    private final ContactTypeLocaleService contactTypeLocaleService;
    private final LocaleService localeService;

    public ContactTypeLocaleController(ContactTypeService contactTypeService,
                                       ContactTypeLocaleService contactTypeLocaleService,
                                       LocaleService localeService) {
        this.contactTypeService = contactTypeService;
        this.contactTypeLocaleService = contactTypeLocaleService;
        this.localeService = localeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @PathVariable("contact-type-id") Long contactTypeId,
            @Valid @RequestBody CreateContactTypeLocaleRequest request) {
        ContactTypeEntity contactType = contactTypeService.getEntityById(contactTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactTypeLocaleService.create(contactType, localeEntity, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable("contact-type-id") Long contactTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactTypeLocaleRequest request) {
        ContactTypeLocaleEntity entity = contactTypeLocaleService.getEntityById(contactTypeId, id);
        return ResponseEntity.ok(contactTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable("contact-type-id") Long contactTypeId,
            @PathVariable Long id) {
        ContactTypeLocaleEntity entity = contactTypeLocaleService.getEntityById(contactTypeId, id);
        return ResponseEntity.ok(contactTypeLocaleService.delete(entity));
    }
}
