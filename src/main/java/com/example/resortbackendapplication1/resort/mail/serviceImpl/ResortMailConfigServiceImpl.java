package com.example.resortbackendapplication1.resort.mail.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.model.mapper.MailProviderMapper;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.CreateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigFilterRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.UpdateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.response.resortmailconfigs.ResortMailConfigResponse;
import com.example.resortbackendapplication1.resort.mail.model.dto.ResortMailConfigDto;
import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;
import com.example.resortbackendapplication1.resort.mail.model.enums.ResortMailConfigSearchField;
import com.example.resortbackendapplication1.resort.mail.model.enums.ResortMailConfigSortField;
import com.example.resortbackendapplication1.resort.mail.model.mapper.ResortMailConfigMapper;
import com.example.resortbackendapplication1.resort.mail.repository.ResortMailConfigRepository;
import com.example.resortbackendapplication1.resort.mail.service.ResortMailConfigService;
import com.example.resortbackendapplication1.resort.mail.specification.ResortMailConfigSpecification;
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
public class ResortMailConfigServiceImpl implements ResortMailConfigService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortMailConfigSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortMailConfigSearchField.allowedFields();

    private final ResortMailConfigRepository resortMailConfigRepository;

    public ResortMailConfigServiceImpl(ResortMailConfigRepository resortMailConfigRepository) {
        this.resortMailConfigRepository = resortMailConfigRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortMailConfigRequest request,
                                  ResortEntity resortEntity,
                                  MailProviderEntity mailProviderEntity) {
        if (resortMailConfigRepository.existsByResortEntity_IdAndNameAndIsActiveAndIsDeleted(
                resortEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("ResortMailConfig with name '" + request.getName() + "' already exists for this resort");
        }

        ResortMailConfigEntity entity = ResortMailConfigMapper.create(request);
        resortEntity.addResortMailConfigEntity(entity);
        entity.setMailProviderEntity(mailProviderEntity);

        resortMailConfigRepository.save(entity);
        log.info("ResortMailConfig created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortMailConfigEntity getEntityById(Long resortId, Long id) {
        return resortMailConfigRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortMailConfig not found with id: " + id));
    }

    @Override
    public ResortMailConfigResponse getById(Long resortId, Long id) {
        ResortMailConfigEntity entity = getEntityById(resortId, id);
        return new ResortMailConfigResponse(buildDto(entity));
    }

    @Override
    public PaginatedResponse<ResortMailConfigDto> getAll(Long resortId, ResortMailConfigFilterRequest request) {
        Specification<@NonNull ResortMailConfigEntity> specification =
                ResortMailConfigSpecification.filter(request, resortId);
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortMailConfigSortField.localeSortFields());
        Page<@NonNull ResortMailConfigDto> page = resortMailConfigRepository
                .findAll(specification, pageable)
                .map(this::buildDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortMailConfigEntity entity, UpdateResortMailConfigRequest request) {
        if (resortMailConfigRepository.existsByResortEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getResortEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("ResortMailConfig with name '" + request.getName() + "' already exists for this resort");
        }

        ResortMailConfigMapper.update(entity, request);
        resortMailConfigRepository.save(entity);
        log.info("ResortMailConfig updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortMailConfigEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortMailConfigRepository.save(entity);
        log.info("ResortMailConfig soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private ResortMailConfigDto buildDto(ResortMailConfigEntity entity) {
        ResortDto resort = ResortMapper.toDto(entity.getResortEntity()).build();
        MailProviderDto mailProvider = MailProviderMapper.toDto(entity.getMailProviderEntity()).build();
        return ResortMailConfigMapper.toDto(entity)
                .resort(resort)
                .mailProvider(mailProvider)
                .build();
    }
}
