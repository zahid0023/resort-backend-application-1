package com.example.resortbackendapplication1.mail.provider.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.CreateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.UpdateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.model.enums.MailProviderConfigSearchField;
import com.example.resortbackendapplication1.mail.provider.model.enums.MailProviderConfigSortField;
import com.example.resortbackendapplication1.mail.provider.model.mapper.MailProviderConfigMapper;
import com.example.resortbackendapplication1.mail.provider.repository.MailProviderConfigRepository;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigService;
import com.example.resortbackendapplication1.mail.provider.specification.MailProviderConfigSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class MailProviderConfigServiceImpl implements MailProviderConfigService {

    private static final Set<String> ALLOWED_SORT_FIELDS = MailProviderConfigSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = MailProviderConfigSearchField.allowedFields();

    private final MailProviderConfigRepository mailProviderConfigRepository;

    public MailProviderConfigServiceImpl(MailProviderConfigRepository mailProviderConfigRepository) {
        this.mailProviderConfigRepository = mailProviderConfigRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateMailProviderConfigRequest request, MailProviderEntity providerEntity) {
        if (mailProviderConfigRepository.existsByMailProviderEntity_IdAndNameAndIsActiveAndIsDeleted(
                providerEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("MailProviderConfig with name '" + request.getName() + "' already exists for this provider");
        }
        if (request.getCode() != null
                && mailProviderConfigRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("MailProviderConfig with code '" + request.getCode() + "' already exists");
        }

        MailProviderConfigEntity entity = MailProviderConfigMapper.create(request);
        providerEntity.addMailProviderConfigEntity(entity);
        mailProviderConfigRepository.save(entity);
        log.info("MailProviderConfig created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public MailProviderConfigEntity getEntityById(Long mailProviderId, Long id) {
        return mailProviderConfigRepository
                .findByMailProviderEntity_IdAndIdAndIsActiveAndIsDeleted(mailProviderId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("MailProviderConfig not found with id: " + id));
    }

    @Override
    public MailProviderConfigEntity getEntityById(Long id) {
        return mailProviderConfigRepository
                .findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("MailProviderConfig not found with id: " + id));
    }

    @Override
    public Optional<MailProviderConfigEntity> getEntityByCode(MailProviderConfigCode code) {
        return mailProviderConfigRepository.findByCodeAndIsActiveAndIsDeleted(code, true, false);
    }

    @Override
    public PaginatedResponse<MailProviderConfigDto> getAll(MailProviderConfigFilterRequest request) {
        Specification<@NonNull MailProviderConfigEntity> specification =
                MailProviderConfigSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull MailProviderConfigDto> page = mailProviderConfigRepository
                .findAll(specification, pageable)
                .map(entity -> MailProviderConfigMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(MailProviderConfigEntity entity, UpdateMailProviderConfigRequest request) {
        if (mailProviderConfigRepository.existsByMailProviderEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getMailProviderEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("MailProviderConfig with name '" + request.getName() + "' already exists for this provider");
        }
        if (request.getCode() != null
                && mailProviderConfigRepository.existsByCodeAndIdNotAndIsActiveAndIsDeleted(request.getCode(), entity.getId(), true, false)) {
            throw new IllegalStateException("MailProviderConfig with code '" + request.getCode() + "' already exists");
        }

        MailProviderConfigMapper.update(entity, request);
        mailProviderConfigRepository.save(entity);
        log.info("MailProviderConfig updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(MailProviderConfigEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        mailProviderConfigRepository.save(entity);
        log.info("MailProviderConfig soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
