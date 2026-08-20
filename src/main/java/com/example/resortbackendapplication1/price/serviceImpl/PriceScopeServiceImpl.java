package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.pricescope.CreatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.UpdatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricescopes.PriceScopeCountResponse;
import com.example.resortbackendapplication1.price.dto.response.pricescopes.PriceScopeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceScopeSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceScopeSortField;
import com.example.resortbackendapplication1.price.model.mapper.PriceScopeLocaleMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceScopeMapper;
import com.example.resortbackendapplication1.price.repository.PriceScopeRepository;
import com.example.resortbackendapplication1.price.service.PriceScopeService;
import com.example.resortbackendapplication1.price.specification.PriceScopeSpecification;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PriceScopeServiceImpl implements PriceScopeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PriceScopeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PriceScopeSearchField.allowedFields();

    private final PriceScopeRepository priceScopeRepository;

    public PriceScopeServiceImpl(PriceScopeRepository priceScopeRepository) {
        this.priceScopeRepository = priceScopeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceScopeRequest request, LocaleEntity localeEntity) {
        if (priceScopeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("PriceScope with code '" + request.getCode() + "' already exists");
        }

        PriceScopeEntity entity = PriceScopeMapper.create(request);

        PriceScopeLocaleEntity priceScopeLocaleEntity = PriceScopeLocaleMapper.create(request.getLocale());
        localeEntity.addPriceScopeLocaleEntity(priceScopeLocaleEntity);
        entity.addPriceScopeLocaleEntity(priceScopeLocaleEntity);

        priceScopeRepository.save(entity);
        log.info("PriceScope created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceScopeEntity getEntityById(Long id) {
        return priceScopeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceScope not found with id: " + id));
    }

    @Override
    public List<PriceScopeEntity> getAll(Set<Long> ids) {
        List<PriceScopeEntity> priceScopeEntities = priceScopeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, priceScopeEntities, PriceScopeEntity::getId, "PriceScope");
        return priceScopeEntities;
    }

    @Override
    public PriceScopeResponse getById(Long id) {
        PriceScopeEntity entity = getEntityById(id);
        PriceScopeDto dto = PriceScopeMapper.toDto(entity).build();
        return new PriceScopeResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceScopeDto> getAll(PriceScopeFilterRequest request) {
        Specification<@NonNull PriceScopeEntity> specification =
                PriceScopeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PriceScopeSortField.localeSortFields());
        Page<@NonNull PriceScopeDto> page = priceScopeRepository
                .findAll(specification, pageable)
                .map(entity -> PriceScopeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public PriceScopeCountResponse getActiveCount() {
        List<String> codes = priceScopeRepository.findCodeByIsActiveAndIsDeleted(true, false);
        return new PriceScopeCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceScopeEntity entity, UpdatePriceScopeRequest request) {
        PriceScopeMapper.update(entity, request);
        priceScopeRepository.save(entity);
        log.info("PriceScope updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceScopeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getPriceScopeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        priceScopeRepository.save(entity);
        log.info("PriceScope soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
