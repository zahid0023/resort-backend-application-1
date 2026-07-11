package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.dto.request.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.response.CommunicationChannelResponse;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.enums.CommunicationChannelSearchField;
import com.example.resortbackendapplication1.contact.model.enums.CommunicationChannelSortField;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelMapper;
import com.example.resortbackendapplication1.contact.repository.CommunicationChannelRepository;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.contact.specification.CommunicationChannelSpecification;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
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
public class CommunicationChannelServiceImpl implements CommunicationChannelService {

    private static final Set<String> ALLOWED_SORT_FIELDS = CommunicationChannelSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = CommunicationChannelSearchField.allowedFields();

    private final CommunicationChannelRepository communicationChannelRepository;

    public CommunicationChannelServiceImpl(CommunicationChannelRepository communicationChannelRepository) {
        this.communicationChannelRepository = communicationChannelRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateCommunicationChannelRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        CommunicationChannelEntity entity = CommunicationChannelMapper.create(request, localeEntityMap);
        communicationChannelRepository.save(entity);
        log.info("CommunicationChannel created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CommunicationChannelEntity getEntityById(Long id) {
        return communicationChannelRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("CommunicationChannel not found with id: " + id));
    }

    @Override
    public CommunicationChannelResponse getById(Long id) {
        CommunicationChannelEntity entity = getEntityById(id);
        return new CommunicationChannelResponse(CommunicationChannelMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<CommunicationChannelDto> getAll(CommunicationChannelFilterRequest request) {
        Page<@NonNull CommunicationChannelDto> page = communicationChannelRepository
                .findAll(CommunicationChannelSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(CommunicationChannelMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(CommunicationChannelEntity entity, UpdateCommunicationChannelRequest request) {
        CommunicationChannelMapper.update(entity, request);
        communicationChannelRepository.save(entity);
        log.info("CommunicationChannel updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        CommunicationChannelEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        communicationChannelRepository.save(entity);
        log.info("CommunicationChannel soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<CommunicationChannelEntity> getAll(Set<Long> ids) {
        List<CommunicationChannelEntity> entities = communicationChannelRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, CommunicationChannelEntity::getId, "CommunicationChannel");
        return entities;
    }
}
