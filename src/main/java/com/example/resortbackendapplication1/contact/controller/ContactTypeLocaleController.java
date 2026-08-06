package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.contact.service.ContactTypeLocaleService;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> create(
            @PathVariable("contact-type-id") Long contactTypeId,
            @Valid @RequestBody CreateContactTypeLocaleRequest request) {
        ContactTypeEntity contactTypeEntity = contactTypeService.getEntityById(contactTypeId);
        LocaleEntity localeEntity = localeService.getEntityById(request.getLocaleId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactTypeLocaleService.create(request, contactTypeEntity, localeEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("contact-type-id") Long contactTypeId,
            @RequestParam(value = "localeCode", required = false) String localeCode,
            @ParameterObject PaginatedRequest paginatedRequest) {
        contactTypeService.getEntityById(contactTypeId);
        return ResponseEntity.ok(contactTypeLocaleService.getAll(contactTypeId, localeCode, paginatedRequest));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(@PathVariable("contact-type-id") Long contactTypeId) {
        contactTypeService.getEntityById(contactTypeId);
        return ResponseEntity.ok(contactTypeLocaleService.getCount(contactTypeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("contact-type-id") Long contactTypeId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactTypeLocaleRequest request) {
        ContactTypeLocaleEntity entity = contactTypeLocaleService.getEntityById(contactTypeId, id);
        return ResponseEntity.ok(contactTypeLocaleService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("contact-type-id") Long contactTypeId,
            @PathVariable Long id) {
        ContactTypeLocaleEntity entity = contactTypeLocaleService.getEntityById(contactTypeId, id);
        return ResponseEntity.ok(contactTypeLocaleService.delete(entity));
    }
}
