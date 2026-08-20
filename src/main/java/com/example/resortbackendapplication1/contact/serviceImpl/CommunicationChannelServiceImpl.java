package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CommunicationChannelFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.CreateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.UpdateCommunicationChannelRequest;
import com.example.resortbackendapplication1.contact.dto.response.communicationchannels.CommunicationChannelResponse;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.model.enums.CommunicationChannelSearchField;
import com.example.resortbackendapplication1.contact.model.enums.CommunicationChannelSortField;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelLocaleMapper;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelMapper;
import com.example.resortbackendapplication1.contact.repository.CommunicationChannelRepository;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelService;
import com.example.resortbackendapplication1.contact.specification.CommunicationChannelSpecification;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SuccessResponse create(CreateCommunicationChannelRequest request, LocaleEntity localeEntity) {
        if (communicationChannelRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("CommunicationChannel with code '" + request.getCode() + "' already exists");
        }

        CommunicationChannelEntity entity = CommunicationChannelMapper.create(request);

        CommunicationChannelLocaleEntity communicationChannelLocaleEntity = CommunicationChannelLocaleMapper.create(request.getLocale());
        localeEntity.addCommunicationChannelLocaleEntity(communicationChannelLocaleEntity);

        entity.addCommunicationChannelLocaleEntity(communicationChannelLocaleEntity);

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
        CommunicationChannelDto dto = CommunicationChannelMapper.toDto(entity).build();
        return new CommunicationChannelResponse(dto);
    }

    @Override
    public PaginatedResponse<CommunicationChannelDto> getAll(CommunicationChannelFilterRequest request) {
        Specification<@NonNull CommunicationChannelEntity> specification = CommunicationChannelSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, CommunicationChannelSortField.localeSortFields());
        Page<@NonNull CommunicationChannelDto> page = communicationChannelRepository
                .findAll(specification, pageable)
                .map(entity -> CommunicationChannelMapper.toDto(entity).build());
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
    public SuccessResponse delete(CommunicationChannelEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getCommunicationChannelLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        communicationChannelRepository.save(entity);
        log.info("CommunicationChannel soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
