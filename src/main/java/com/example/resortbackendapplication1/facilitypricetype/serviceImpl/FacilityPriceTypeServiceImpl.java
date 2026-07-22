package com.example.resortbackendapplication1.facilitypricetype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.response.FacilityPriceTypeResponse;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.enums.FacilityPriceTypeSearchField;
import com.example.resortbackendapplication1.facilitypricetype.model.enums.FacilityPriceTypeSortField;
import com.example.resortbackendapplication1.facilitypricetype.model.mapper.FacilityPriceTypeMapper;
import com.example.resortbackendapplication1.facilitypricetype.repository.FacilityPriceTypeRepository;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeService;
import com.example.resortbackendapplication1.facilitypricetype.specification.FacilityPriceTypeSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
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
    public SuccessResponse create(CreateFacilityPriceTypeRequest request) {
        FacilityPriceTypeEntity entity = FacilityPriceTypeMapper.create(request);
        facilityPriceTypeRepository.save(entity);
        log.info("FacilityPriceType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityPriceTypeEntity getEntityById(Long id) {
        return facilityPriceTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityPriceType not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityPriceTypeResponse getById(Long id) {
        FacilityPriceTypeEntity entity = getEntityById(id);
        FacilityPriceTypeDto dto = FacilityPriceTypeMapper.toDto(entity);
        return new FacilityPriceTypeResponse(dto);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<FacilityPriceTypeDto> getAll(FacilityPriceTypeFilterRequest request) {
        Page<@NonNull FacilityPriceTypeDto> page = facilityPriceTypeRepository
                .findAll(FacilityPriceTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(FacilityPriceTypeMapper::toDto);
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
    public SuccessResponse delete(Long id) {
        FacilityPriceTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityPriceTypeRepository.save(entity);
        log.info("FacilityPriceType soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }
}
