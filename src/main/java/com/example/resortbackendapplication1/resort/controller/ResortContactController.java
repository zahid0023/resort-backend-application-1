package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.CreateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortContactEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortContactService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/contacts")
public class ResortContactController {

    private final ResortContactService resortContactService;
    private final ResortService resortService;
    private final ContactTypeService contactTypeService;
    private final CommunicationChannelService communicationChannelService;

    public ResortContactController(ResortContactService resortContactService,
                                   ResortService resortService,
                                   ContactTypeService contactTypeService,
                                   CommunicationChannelService communicationChannelService) {
        this.resortContactService = resortContactService;
        this.resortService = resortService;
        this.contactTypeService = contactTypeService;
        this.communicationChannelService = communicationChannelService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortContactRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        ContactTypeEntity contactTypeEntity = contactTypeService.getEntityById(request.getContactTypeId());
        CommunicationChannelEntity communicationChannelEntity =
                communicationChannelService.getEntityById(request.getCommunicationChannelId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortContactService.create(request, resortEntity, contactTypeEntity, communicationChannelEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortContactService.getById(resortId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortContactFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortContactService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortContactRequest request) {
        ResortContactEntity entity = resortContactService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortContactService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortContactEntity entity = resortContactService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortContactService.delete(entity));
    }
}
