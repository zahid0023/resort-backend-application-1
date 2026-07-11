package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.LocaleUtils;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRepository;
import com.example.resortbackendapplication1.resort.repository.ResortUserPermissionRepository;
import com.example.resortbackendapplication1.resort.repository.ResortUserRepository;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.specification.ResortSpecification;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.repository.ResortAccessTypeRepository;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoService;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.service.ResortContactService;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.repository.ResortPermissionTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResortServiceImpl implements ResortService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortSearchField.allowedFields();

    private final ResortRepository resortRepository;
    private final ResortUserRepository resortUserRepository;
    private final ResortUserPermissionRepository resortUserPermissionRepository;
    private final ResortAccessTypeRepository resortAccessTypeRepository;
    private final ResortPermissionTypeRepository resortPermissionTypeRepository;
    private final UserService userService;
    private final ResortBasicInfoService resortBasicInfoService;
    private final ResortContactService resortContactService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocaleService localeService;
    private final ContactTypeService contactTypeService;
    private final CommunicationChannelService communicationChannelService;

    public ResortServiceImpl(ResortRepository resortRepository,
                             ResortUserRepository resortUserRepository,
                             ResortUserPermissionRepository resortUserPermissionRepository,
                             ResortAccessTypeRepository resortAccessTypeRepository,
                             ResortPermissionTypeRepository resortPermissionTypeRepository,
                             UserService userService,
                             ResortBasicInfoService resortBasicInfoService,
                             ResortContactService resortContactService,
                             CountryService countryService,
                             CityService cityService,
                             LocaleService localeService,
                             ContactTypeService contactTypeService,
                             CommunicationChannelService communicationChannelService) {
        this.resortRepository = resortRepository;
        this.resortUserRepository = resortUserRepository;
        this.resortUserPermissionRepository = resortUserPermissionRepository;
        this.resortAccessTypeRepository = resortAccessTypeRepository;
        this.resortPermissionTypeRepository = resortPermissionTypeRepository;
        this.userService = userService;
        this.resortBasicInfoService = resortBasicInfoService;
        this.resortContactService = resortContactService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.localeService = localeService;
        this.contactTypeService = contactTypeService;
        this.communicationChannelService = communicationChannelService;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRequest request) {
        UserEntity currentUser = userService.getAuthenticatedUserEntity();

        ResortEntity resort = ResortMapper.create(request);
        resortRepository.save(resort);

        ResortAccessTypeEntity ownerAccessType = resortAccessTypeRepository
                .findByCodeAndIsActiveAndIsDeleted("OWNER", true, false)
                .orElseThrow(() -> new EntityNotFoundException("OWNER access type not found"));

        ResortUserEntity resortUser = new ResortUserEntity();
        resortUser.setResortEntity(resort);
        resortUser.setUserEntity(currentUser);
        resortUser.setResortAccessTypeEntity(ownerAccessType);
        resortUser.setJoinedAt(Instant.now());
        resortUserRepository.save(resortUser);

        List<ResortPermissionTypeEntity> allPermissions =
                resortPermissionTypeRepository.findAllByIsActiveAndIsDeleted(true, false);

        List<ResortUserPermissionEntity> permissions = allPermissions.stream()
                .map(pt -> {
                    ResortUserPermissionEntity perm = new ResortUserPermissionEntity();
                    perm.setResortUserEntity(resortUser);
                    perm.setResortPermissionTypeEntity(pt);
                    perm.setIsAllowed(true);
                    return perm;
                }).toList();

        resortUserPermissionRepository.saveAll(permissions);

        if (request.getBasicInfo() != null) {
            CreateResortBasicInfoRequest basicInfo = request.getBasicInfo();
            CountryEntity countryEntity = countryService.getEntityById(basicInfo.getCountryId());
            CityEntity cityEntity = cityService.getEntityById(basicInfo.getCityId());
            Map<Long, LocaleEntity> localeEntityMap = LocaleUtils.resolveLocaleMap(
                    basicInfo.getLocales(), CreateResortBasicInfoLocaleRequest::getLocaleId, localeService);
            resortBasicInfoService.create(basicInfo, resort, countryEntity, cityEntity, localeEntityMap);
        }

        if (request.getContacts() != null && !request.getContacts().isEmpty()) {
            Set<Long> contactTypeIds = request.getContacts().stream()
                    .map(CreateResortContactRequest::getContactTypeId)
                    .collect(Collectors.toSet());
            Set<Long> channelIds = request.getContacts().stream()
                    .map(CreateResortContactRequest::getCommunicationChannelId)
                    .collect(Collectors.toSet());

            Map<Long, ContactTypeEntity> contactTypeMap = contactTypeService.getAll(contactTypeIds).stream()
                    .collect(Collectors.toMap(ContactTypeEntity::getId, e -> e));
            Map<Long, CommunicationChannelEntity> channelMap = communicationChannelService.getAll(channelIds).stream()
                    .collect(Collectors.toMap(CommunicationChannelEntity::getId, e -> e));

            for (CreateResortContactRequest contact : request.getContacts()) {
                resortContactService.create(
                        resort,
                        contactTypeMap.get(contact.getContactTypeId()),
                        channelMap.get(contact.getCommunicationChannelId()),
                        contact);
            }
        }

        log.info("Resort created with id: {}, owner user id: {}", resort.getId(), currentUser.getId());
        return new SuccessResponse(true, resort.getId());
    }

    @Override
    public ResortEntity getEntityById(Long id) {
        return resortRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort not found with id: " + id));
    }

    @Override
    public ResortResponse getById(Long id) {
        ResortEntity entity = getEntityById(id);
        return new ResortResponse(ResortMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortDto> getAll(ResortFilterRequest request) {
        Page<@NonNull ResortDto> page = resortRepository
                .findAll(ResortSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public PaginatedResponse<ResortDto> getAllForUser(ResortFilterRequest request, Long userId) {
        Page<@NonNull ResortDto> page = resortRepository
                .findAll(ResortSpecification.filter(request, userId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortEntity entity, UpdateResortRequest request) {
        ResortMapper.update(entity, request);
        resortRepository.save(entity);
        log.info("Resort updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        UserEntity currentUser = userService.getAuthenticatedUserEntity();
        ResortEntity entity = getEntityById(id);
        boolean isOwner = resortUserRepository.existsByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(
                id, currentUser.getId(), true, false);
        if (!isOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not a member of resort with id: " + id);
        }
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRepository.save(entity);
        log.info("Resort soft-deleted with id: {}, by user id: {}", id, currentUser.getId());
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortEntity> getAll(Set<Long> ids) {
        List<ResortEntity> entities = resortRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortEntity::getId, "Resort");
        return entities;
    }
}
