package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceTypeSortField;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.specification.PriceTypeSpecification;
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
public class PriceTypeServiceImpl implements PriceTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PriceTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PriceTypeSearchField.allowedFields();

    private final PriceTypeRepository priceTypeRepository;

    public PriceTypeServiceImpl(PriceTypeRepository priceTypeRepository) {
        this.priceTypeRepository = priceTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceTypeRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        PriceTypeEntity entity = PriceTypeMapper.create(request, localeEntityMap);
        priceTypeRepository.save(entity);
        log.info("PriceType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeResponse getById(Long id) {
        PriceTypeEntity entity = getEntityById(id);
        PriceTypeDto dto = PriceTypeMapper.toDto(entity);
        return new PriceTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceTypeDto> getAll(PriceTypeFilterRequest request) {
        Page<@NonNull PriceTypeDto> page = priceTypeRepository
                .findAll(PriceTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(PriceTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceTypeEntity entity, UpdatePriceTypeRequest request) {
        PriceTypeMapper.update(entity, request);
        priceTypeRepository.save(entity);
        log.info("PriceType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        PriceTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeRepository.save(entity);
        log.info("PriceType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeEntity getEntityById(Long id) {
        return priceTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceType not found with id: " + id));
    }

    @Override
    public List<PriceTypeEntity> getAll(Set<Long> ids) {
        List<PriceTypeEntity> entities = priceTypeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, PriceTypeEntity::getId, "PriceType");
        return entities;
    }
}
