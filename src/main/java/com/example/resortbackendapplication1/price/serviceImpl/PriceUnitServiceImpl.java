package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.response.priceunits.PriceUnitResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.enums.PriceUnitSearchField;
import com.example.resortbackendapplication1.price.model.enums.PriceUnitSortField;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.price.repository.PriceUnitRepository;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.price.specification.PriceUnitSpecification;
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
public class PriceUnitServiceImpl implements PriceUnitService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PriceUnitSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PriceUnitSearchField.allowedFields();

    private final PriceUnitRepository priceUnitRepository;

    public PriceUnitServiceImpl(PriceUnitRepository priceUnitRepository) {
        this.priceUnitRepository = priceUnitRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceUnitRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        PriceUnitEntity entity = PriceUnitMapper.create(request, localeEntityMap);
        priceUnitRepository.save(entity);
        log.info("PriceUnit created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitResponse getById(Long id) {
        PriceUnitEntity entity = getEntityById(id);
        PriceUnitDto dto = PriceUnitMapper.toDto(entity);
        return new PriceUnitResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceUnitDto> getAll(PriceUnitFilterRequest request) {
        Page<@NonNull PriceUnitDto> page = priceUnitRepository
                .findAll(PriceUnitSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(PriceUnitMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
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
    public SuccessResponse delete(Long id) {
        PriceUnitEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceUnitRepository.save(entity);
        log.info("PriceUnit soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitEntity getEntityById(Long id) {
        return priceUnitRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceUnit not found with id: " + id));
    }

    @Override
    public List<PriceUnitEntity> getAll(Set<Long> ids) {
        List<PriceUnitEntity> entities = priceUnitRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, PriceUnitEntity::getId, "PriceUnit");
        return entities;
    }
}
