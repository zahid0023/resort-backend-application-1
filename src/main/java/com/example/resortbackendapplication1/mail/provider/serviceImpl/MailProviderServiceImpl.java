package com.example.resortbackendapplication1.mail.provider.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.CreateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderFilterRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.UpdateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.CreateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.response.mailproviders.MailProviderResponse;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.model.enums.MailProviderSearchField;
import com.example.resortbackendapplication1.mail.provider.model.enums.MailProviderSortField;
import com.example.resortbackendapplication1.mail.provider.model.mapper.MailProviderConfigFieldMapper;
import com.example.resortbackendapplication1.mail.provider.model.mapper.MailProviderMapper;
import com.example.resortbackendapplication1.mail.provider.repository.MailProviderRepository;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderService;
import com.example.resortbackendapplication1.mail.provider.specification.MailProviderSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class MailProviderServiceImpl implements MailProviderService {

    private static final Set<String> ALLOWED_SORT_FIELDS = MailProviderSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = MailProviderSearchField.allowedFields();

    private final MailProviderRepository mailProviderRepository;

    public MailProviderServiceImpl(MailProviderRepository mailProviderRepository) {
        this.mailProviderRepository = mailProviderRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateMailProviderRequest request) {
        if (mailProviderRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("MailProvider with code '" + request.getCode() + "' already exists");
        }

        Set<String> seenKeys = new HashSet<>();
        for (CreateMailProviderConfigFieldRequest fieldRequest : request.getConfigFields()) {
            if (!seenKeys.add(fieldRequest.getKey())) {
                throw new IllegalStateException("Duplicate config field key '" + fieldRequest.getKey() + "' in request");
            }
        }

        MailProviderEntity entity = MailProviderMapper.create(request);
        for (CreateMailProviderConfigFieldRequest fieldRequest : request.getConfigFields()) {
            entity.addMailProviderConfigFieldEntity(MailProviderConfigFieldMapper.create(fieldRequest));
        }
        mailProviderRepository.save(entity);
        log.info("MailProvider created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public MailProviderEntity getEntityById(Long id) {
        return mailProviderRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("MailProvider not found with id: " + id));
    }

    @Override
    public MailProviderResponse getById(Long id) {
        MailProviderEntity entity = getEntityById(id);
        MailProviderDto dto = MailProviderMapper.toDto(entity).build();
        return new MailProviderResponse(dto);
    }

    @Override
    public PaginatedResponse<MailProviderDto> getAll(MailProviderFilterRequest request) {
        Specification<@NonNull MailProviderEntity> specification =
                MailProviderSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull MailProviderDto> page = mailProviderRepository
                .findAll(specification, pageable)
                .map(entity -> MailProviderMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(MailProviderEntity entity, UpdateMailProviderRequest request) {
        MailProviderMapper.update(entity, request);
        mailProviderRepository.save(entity);
        log.info("MailProvider updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(MailProviderEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        mailProviderRepository.save(entity);
        log.info("MailProvider soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
