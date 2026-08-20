package com.example.resortbackendapplication1.bedtype.serviceImpl;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.CreateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.UpdateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.response.bedtypes.BedTypeResponse;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.bedtype.model.enums.BedTypeSearchField;
import com.example.resortbackendapplication1.bedtype.model.enums.BedTypeSortField;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeLocaleMapper;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.bedtype.repository.BedTypeRepository;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.bedtype.specification.BedTypeSpecification;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
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
public class BedTypeServiceImpl implements BedTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = BedTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = BedTypeSearchField.allowedFields();

    private final BedTypeRepository bedTypeRepository;

    public BedTypeServiceImpl(BedTypeRepository bedTypeRepository) {
        this.bedTypeRepository = bedTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateBedTypeRequest request, LocaleEntity localeEntity) {
        if (bedTypeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("BedType with code '" + request.getCode() + "' already exists");
        }

        BedTypeEntity entity = BedTypeMapper.create(request);

        BedTypeLocaleEntity bedTypeLocaleEntity = BedTypeLocaleMapper.create(request.getLocale());
        localeEntity.addBedTypeLocaleEntity(bedTypeLocaleEntity);

        entity.addBedTypeLocaleEntity(bedTypeLocaleEntity);

        bedTypeRepository.save(entity);
        log.info("BedType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BedTypeEntity getEntityById(Long id) {
        return bedTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BedType not found with id: " + id));
    }

    @Override
    public BedTypeResponse getById(Long id) {
        BedTypeEntity entity = getEntityById(id);
        BedTypeDto dto = BedTypeMapper.toDto(entity).build();
        return new BedTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<BedTypeDto> getAll(BedTypeFilterRequest request) {
        Specification<@NonNull BedTypeEntity> specification = BedTypeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, BedTypeSortField.localeSortFields());
        Page<@NonNull BedTypeDto> page = bedTypeRepository
                .findAll(specification, pageable)
                .map(entity -> BedTypeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(BedTypeEntity entity, UpdateBedTypeRequest request) {
        BedTypeMapper.update(entity, request);
        bedTypeRepository.save(entity);
        log.info("BedType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(BedTypeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getBedTypeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        bedTypeRepository.save(entity);
        log.info("BedType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
