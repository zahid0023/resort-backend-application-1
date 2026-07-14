package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRepository;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.specification.ResortSpecification;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ResortServiceImpl implements ResortService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortSearchField.allowedFields();

    private final ResortRepository resortRepository;

    public ResortServiceImpl(ResortRepository resortRepository) {
        this.resortRepository = resortRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRequest request,
                                  UserEntity userEntity,
                                  ResortAccessTypeEntity resortAccessTypeEntity,
                                  ResortPermissionTypeEntity resortPermissionTypeEntity,
                                  CountryEntity countryEntity,
                                  CityEntity cityEntity,
                                  Map<Long, LocaleEntity> localeEntityMap,
                                  Map<Long, ContactTypeEntity> contactTypeEntityMap,
                                  Map<Long, CommunicationChannelEntity> communicationChannelEntityMap) {
        ResortEntity resortEntity = ResortMapper.create(request, userEntity, resortAccessTypeEntity, resortPermissionTypeEntity,
                countryEntity, cityEntity, localeEntityMap, contactTypeEntityMap, communicationChannelEntityMap);
        resortRepository.save(resortEntity);
        log.info("Resort created with id: {}, owner user id: {}", resortEntity.getId(), userEntity.getId());
        return new SuccessResponse(true, resortEntity.getId());
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
        Page<@NonNull ResortEntity> page = resortRepository
                .findAll(ResortSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(ResortMapper::toSummaryDto), ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public PaginatedResponse<ResortDto> getAllForUser(ResortFilterRequest request, Long userId) {
        Page<@NonNull ResortEntity> page = resortRepository
                .findAll(ResortSpecification.filter(request, userId), request.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(ResortMapper::toSummaryDto), ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
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
        ResortEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRepository.save(entity);
        log.info("Resort soft-deleted with id: {}", id);
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
