package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.resortfacilitypricetypes.FacilityPriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.model.enums.FacilityPriceTypeSearchField;
import com.example.resortbackendapplication1.price.model.enums.FacilityPriceTypeSortField;
import com.example.resortbackendapplication1.price.model.mapper.FacilityPriceTypeLocaleMapper;
import com.example.resortbackendapplication1.price.model.mapper.FacilityPriceTypeMapper;
import com.example.resortbackendapplication1.price.repository.FacilityPriceTypeRepository;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.price.specification.FacilityPriceTypeSpecification;
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
public class FacilityPriceTypeServiceImpl implements FacilityPriceTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilityPriceTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilityPriceTypeSearchField.allowedFields();

    private final FacilityPriceTypeRepository facilityPriceTypeRepository;

    public FacilityPriceTypeServiceImpl(FacilityPriceTypeRepository facilityPriceTypeRepository) {
        this.facilityPriceTypeRepository = facilityPriceTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityPriceTypeRequest request,
                                  LocaleEntity localeEntity) {
        if (facilityPriceTypeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("FacilityPriceType with code '" + request.getCode() + "' already exists");
        }

        FacilityPriceTypeEntity entity = FacilityPriceTypeMapper.create(request);

        FacilityPriceTypeLocaleEntity facilityPriceTypeLocaleEntity = FacilityPriceTypeLocaleMapper.create(request.getLocale());
        localeEntity.addFacilityPriceTypeLocaleEntity(facilityPriceTypeLocaleEntity);
        entity.addFacilityPriceTypeLocaleEntity(facilityPriceTypeLocaleEntity);

        facilityPriceTypeRepository.save(entity);
        log.info("FacilityPriceType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityPriceTypeEntity getEntityById(Long id) {
        return facilityPriceTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityPriceType not found with id: " + id));
    }

    @Override
    public FacilityPriceTypeEntity getEntityByCode(String code) {
        return facilityPriceTypeRepository.findByCodeAndIsActiveAndIsDeleted(code, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityPriceType not found with code: " + code));
    }

    @Override
    public FacilityPriceTypeResponse getById(Long id) {
        FacilityPriceTypeEntity entity = getEntityById(id);
        FacilityPriceTypeDto dto = FacilityPriceTypeMapper.toDto(entity).build();
        return new FacilityPriceTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityPriceTypeDto> getAll(FacilityPriceTypeFilterRequest request) {
        Specification<@NonNull FacilityPriceTypeEntity> specification =
                FacilityPriceTypeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, FacilityPriceTypeSortField.localeSortFields());
        Page<@NonNull FacilityPriceTypeDto> page = facilityPriceTypeRepository
                .findAll(specification, pageable)
                .map(entity -> FacilityPriceTypeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityPriceTypeEntity entity, UpdateFacilityPriceTypeRequest request) {
        FacilityPriceTypeMapper.update(entity, request);
        facilityPriceTypeRepository.save(entity);
        log.info("FacilityPriceType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityPriceTypeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getFacilityPriceTypeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        facilityPriceTypeRepository.save(entity);
        log.info("FacilityPriceType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
