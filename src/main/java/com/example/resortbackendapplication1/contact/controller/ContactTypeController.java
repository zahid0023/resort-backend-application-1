package com.example.resortbackendapplication1.contact.controller;

import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.contact.dto.request.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contact-types")
public class ContactTypeController {

    private final ContactTypeService contactTypeService;
    private final LocaleService localeService;

    public ContactTypeController(ContactTypeService contactTypeService,
                                 LocaleService localeService) {
        this.contactTypeService = contactTypeService;
        this.localeService = localeService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateContactTypeRequest request) {
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                request.getLocales(), CreateContactTypeLocaleRequest::getLocaleId, localeService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactTypeService.create(request, localeEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contactTypeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ContactTypeFilterRequest request) {
        return ResponseEntity.ok(contactTypeService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactTypeRequest request) {
        ContactTypeEntity entity = contactTypeService.getEntityById(id);
        return ResponseEntity.ok(contactTypeService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(contactTypeService.delete(id));
    }
}
