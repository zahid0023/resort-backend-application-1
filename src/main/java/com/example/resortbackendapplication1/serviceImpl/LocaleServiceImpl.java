package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.locale.CreateLocaleRequest;
import com.example.resortbackendapplication1.dto.request.locale.UpdateLocaleRequest;
import com.example.resortbackendapplication1.dto.response.locales.LocaleResponse;
import com.example.resortbackendapplication1.model.dto.LocaleDto;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.enums.LocaleSortField;
import com.example.resortbackendapplication1.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.model.projection.LocaleSummary;
import com.example.resortbackendapplication1.repository.LocaleRepository;
import com.example.resortbackendapplication1.service.LocaleService;
import com.example.resortbackendapplication1.utils.Pagination;
import com.example.resortbackendapplication1.validation.EntityValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class LocaleServiceImpl implements LocaleService {

    private static final Set<String> ALLOWED_SORT_FIELDS = LocaleSortField.allowedFields();

    private final LocaleRepository localeRepository;

    public LocaleServiceImpl(LocaleRepository localeRepository) {
        this.localeRepository = localeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateLocaleRequest request) {
        LocaleEntity entity = LocaleMapper.fromRequest(request);
        localeRepository.save(entity);
        log.info("Locale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public LocaleResponse getById(Long id) {
        LocaleEntity entity = getEntityById(id);
        LocaleDto dto = LocaleMapper.toDto(entity);
        return new LocaleResponse(dto);
    }

    @Override
    public PaginatedResponse<LocaleSummary> getAll(PaginatedRequest request) {
        Page<@NonNull LocaleSummary> page = localeRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(LocaleEntity entity, UpdateLocaleRequest request) {
        LocaleMapper.update(entity, request);
        localeRepository.save(entity);
        log.info("Locale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        LocaleEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        localeRepository.save(entity);
        log.info("Locale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public LocaleEntity getEntityById(Long id) {
        return localeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Locale not found with id: " + id));
    }

    @Override
    public List<LocaleEntity> getAll(Set<Long> ids) {
        List<LocaleEntity> localeEntities = localeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, localeEntities, LocaleEntity::getId, "Locale");
        return localeEntities;
    }
}
