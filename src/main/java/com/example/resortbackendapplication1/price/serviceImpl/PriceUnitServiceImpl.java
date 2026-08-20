package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.response.priceunits.PriceUnitResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceUnitSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceUnitSortField;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitLocaleMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitScopeAssignmentMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceScopeMapper;
import com.example.resortbackendapplication1.price.repository.PriceUnitRepository;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.price.specification.PriceUnitSpecification;
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
public class PriceUnitServiceImpl implements PriceUnitService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PriceUnitSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PriceUnitSearchField.allowedFields();

    private final PriceUnitRepository priceUnitRepository;

    public PriceUnitServiceImpl(PriceUnitRepository priceUnitRepository) {
        this.priceUnitRepository = priceUnitRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceUnitRequest request,
                                  List<PriceScopeEntity> priceScopeEntities,
                                  LocaleEntity localeEntity) {
        if (priceUnitRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("PriceUnit with code '" + request.getCode() + "' already exists");
        }

        PriceUnitEntity entity = PriceUnitMapper.create(request);

        for (PriceScopeEntity priceScopeEntity : priceScopeEntities) {
            PriceUnitScopeAssignmentEntity assignmentEntity = PriceUnitScopeAssignmentMapper.create();
            priceScopeEntity.addPriceUnitScopeAssignmentEntity(assignmentEntity);
            entity.addPriceUnitScopeAssignmentEntity(assignmentEntity);
        }

        PriceUnitLocaleEntity priceUnitLocaleEntity = PriceUnitLocaleMapper.create(request.getLocale());
        localeEntity.addPriceUnitLocaleEntity(priceUnitLocaleEntity);
        entity.addPriceUnitLocaleEntity(priceUnitLocaleEntity);

        priceUnitRepository.save(entity);
        log.info("PriceUnit created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitEntity getEntityById(Long id) {
        return priceUnitRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceUnit not found with id: " + id));
    }

    @Override
    public PriceUnitResponse getById(Long id) {
        PriceUnitEntity entity = getEntityById(id);
        PriceUnitDto dto = PriceUnitMapper.toDto(entity)
                .priceScopes(mapPriceScopes(entity))
                .build();
        return new PriceUnitResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceUnitDto> getAll(PriceUnitFilterRequest request) {
        Specification<@NonNull PriceUnitEntity> specification =
                PriceUnitSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PriceUnitSortField.localeSortFields());
        Page<@NonNull PriceUnitDto> page = priceUnitRepository
                .findAll(specification, pageable)
                .map(entity -> PriceUnitMapper.toDto(entity)
                        .priceScopes(mapPriceScopes(entity))
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    private List<PriceScopeDto> mapPriceScopes(PriceUnitEntity entity) {
        return entity.getPriceUnitScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(PriceUnitScopeAssignmentEntity::getPriceScopeEntity)
                .map(priceScopeEntity -> PriceScopeMapper.toDto(priceScopeEntity).build())
                .toList();
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceUnitEntity entity, UpdatePriceUnitRequest request) {
        PriceUnitMapper.update(entity, request);
        priceUnitRepository.save(entity);
        log.info("PriceUnit updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceUnitEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getPriceUnitLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        priceUnitRepository.save(entity);
        log.info("PriceUnit soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
