package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.CreatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.UpdatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypescopes.PriceTypeScopeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeScopeSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeScopeSortField;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeScopeLocaleMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeScopeMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeScopeRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeService;
import com.example.resortbackendapplication1.price.specification.PriceTypeScopeSpecification;
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
public class PriceTypeScopeServiceImpl implements PriceTypeScopeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PriceTypeScopeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PriceTypeScopeSearchField.allowedFields();

    private final PriceTypeScopeRepository priceTypeScopeRepository;

    public PriceTypeScopeServiceImpl(PriceTypeScopeRepository priceTypeScopeRepository) {
        this.priceTypeScopeRepository = priceTypeScopeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceTypeScopeRequest request, LocaleEntity localeEntity) {
        if (priceTypeScopeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("PriceTypeScope with code '" + request.getCode() + "' already exists");
        }

        PriceTypeScopeEntity entity = PriceTypeScopeMapper.create(request);

        PriceTypeScopeLocaleEntity priceTypeScopeLocaleEntity = PriceTypeScopeLocaleMapper.create(request.getLocale());
        localeEntity.addPriceTypeScopeLocaleEntity(priceTypeScopeLocaleEntity);
        entity.addPriceTypeScopeLocaleEntity(priceTypeScopeLocaleEntity);

        priceTypeScopeRepository.save(entity);
        log.info("PriceTypeScope created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeScopeEntity getEntityById(Long id) {
        return priceTypeScopeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceTypeScope not found with id: " + id));
    }

    @Override
    public PriceTypeScopeResponse getById(Long id) {
        PriceTypeScopeEntity entity = getEntityById(id);
        PriceTypeScopeDto dto = PriceTypeScopeMapper.toDto(entity).build();
        return new PriceTypeScopeResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceTypeScopeDto> getAll(PriceTypeScopeFilterRequest request) {
        Specification<@NonNull PriceTypeScopeEntity> specification =
                PriceTypeScopeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PriceTypeScopeSortField.localeSortFields());
        Page<@NonNull PriceTypeScopeDto> page = priceTypeScopeRepository
                .findAll(specification, pageable)
                .map(entity -> PriceTypeScopeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceTypeScopeEntity entity, UpdatePriceTypeScopeRequest request) {
        PriceTypeScopeMapper.update(entity, request);
        priceTypeScopeRepository.save(entity);
        log.info("PriceTypeScope updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceTypeScopeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeScopeRepository.save(entity);
        log.info("PriceTypeScope soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
