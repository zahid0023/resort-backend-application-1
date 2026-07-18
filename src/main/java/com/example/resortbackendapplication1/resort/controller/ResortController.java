package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.auth.model.dto.CustomUserDetails;
import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.service.ResortUserService;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeService;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts")
public class ResortController {

    private final ResortService resortService;
    private final ResortUserService resortUserService;
    private final ResortAccessTypeService resortAccessTypeService;
    private final ResortPermissionTypeService resortPermissionTypeService;
    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocaleService localeService;
    private final ContactTypeService contactTypeService;
    private final CommunicationChannelService communicationChannelService;

    public ResortController(ResortService resortService,
                            ResortUserService resortUserService,
                            ResortAccessTypeService resortAccessTypeService,
                            ResortPermissionTypeService resortPermissionTypeService,
                            UserService userService,
                            CountryService countryService,
                            CityService cityService,
                            LocaleService localeService,
                            ContactTypeService contactTypeService,
                            CommunicationChannelService communicationChannelService) {
        this.resortService = resortService;
        this.resortUserService = resortUserService;
        this.resortAccessTypeService = resortAccessTypeService;
        this.resortPermissionTypeService = resortPermissionTypeService;
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.localeService = localeService;
        this.contactTypeService = contactTypeService;
        this.communicationChannelService = communicationChannelService; 
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateResortRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        UserEntity userEntity = userService.getUserById(principal.getId());
        ResortAccessTypeEntity resortAccessTypeEntity = resortAccessTypeService.getEntityByCode("OWNER");
        ResortPermissionTypeEntity resortPermissionTypeEntity = resortPermissionTypeService.getEntityByCode("ALL_PERMISSIONS");

        CreateResortBasicInfoRequest basicInfo = request.getBasicInfo();
        CountryEntity countryEntity = countryService.getEntityById(basicInfo.getCountryId());
        CityEntity cityEntity = cityService.getEntityById(basicInfo.getCityId());
        Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                basicInfo.getLocales(), CreateResortBasicInfoLocaleRequest::getLocaleId, localeService);

        if (request.getContacts().isEmpty())
            throw new IllegalArgumentException("Contacts cannot be empty");

        Set<Long> contactTypeIds = request.getContacts().stream()
                .map(CreateResortContactRequest::getContactTypeId)
                .collect(Collectors.toSet());
        Map<Long, ContactTypeEntity> contactTypeEntityMap = contactTypeService.getAll(contactTypeIds).stream()
                .collect(Collectors.toMap(ContactTypeEntity::getId, e -> e));

        Set<Long> channelIds = request.getContacts().stream()
                .map(CreateResortContactRequest::getCommunicationChannelId)
                .collect(Collectors.toSet());
        Map<Long, CommunicationChannelEntity> communicationChannelEntityMap = communicationChannelService.getAll(channelIds).stream()
                .collect(Collectors.toMap(CommunicationChannelEntity::getId, e -> e));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                resortService.create(request, userEntity, resortAccessTypeEntity, resortPermissionTypeEntity,
                        countryEntity, cityEntity, localeEntityMap, contactTypeEntityMap, communicationChannelEntityMap));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortFilterRequest request) {
        return ResponseEntity.ok(resortService.getAll(request));
    }

    @GetMapping("/my-resorts")
    public ResponseEntity<?> getAllForUser(
            @Valid @ParameterObject ResortFilterRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(resortService.getAllForUser(request, principal.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRequest request) {
        ResortEntity entity = resortService.getEntityById(id);
        return ResponseEntity.ok(resortService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        if (!resortUserService.isOwner(id, principal.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not a member of resort with id: " + id);
        }
        return ResponseEntity.ok(resortService.delete(id));
    }
}
