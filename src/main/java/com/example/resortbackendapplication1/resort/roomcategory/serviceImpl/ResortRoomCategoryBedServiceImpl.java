package com.example.resortbackendapplication1.resort.roomcategory.serviceImpl;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.ResortRoomCategoryBedFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategorybeds.ResortRoomCategoryBedResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryBedSearchField;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryBedSortField;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryBedMapper;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.roomcategory.repository.ResortRoomCategoryBedRepository;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resort.roomcategory.specification.ResortRoomCategoryBedSpecification;
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
public class ResortRoomCategoryBedServiceImpl implements ResortRoomCategoryBedService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategoryBedSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomCategoryBedSearchField.allowedFields();

    private final ResortRoomCategoryBedRepository resortRoomCategoryBedRepository;

    public ResortRoomCategoryBedServiceImpl(ResortRoomCategoryBedRepository resortRoomCategoryBedRepository) {
        this.resortRoomCategoryBedRepository = resortRoomCategoryBedRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryBedRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  BedTypeEntity bedTypeEntity) {
        if (resortRoomCategoryBedRepository.existsByResortRoomCategoryEntity_IdAndBedTypeEntity_IdAndIsActiveAndIsDeleted(
                resortRoomCategoryEntity.getId(), bedTypeEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort room category already has a bed row for bed type id: "
                    + bedTypeEntity.getId());
        }

        ResortRoomCategoryBedEntity entity = ResortRoomCategoryBedMapper.create(request);
        resortRoomCategoryEntity.addResortRoomCategoryBedEntity(entity);
        bedTypeEntity.addResortRoomCategoryBedEntity(entity);

        resortRoomCategoryBedRepository.save(entity);
        log.info("ResortRoomCategoryBed created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryBedEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryBedRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryBed not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryBedResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryBedEntity entity = getEntityById(resortRoomCategoryId, id);
        ResortRoomCategoryBedDto dto = ResortRoomCategoryBedMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                .bedType(BedTypeMapper.toDto(entity.getBedTypeEntity()).build())
                .build();
        return new ResortRoomCategoryBedResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryBedDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryBedFilterRequest request) {
        Specification<@NonNull ResortRoomCategoryBedEntity> specification =
                ResortRoomCategoryBedSpecification.filter(resortRoomCategoryId, request);
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomCategoryBedSortField.localeSortFields());
        Page<@NonNull ResortRoomCategoryBedDto> page = resortRoomCategoryBedRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomCategoryBedMapper.toDto(entity)
                        .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                        .bedType(BedTypeMapper.toDto(entity.getBedTypeEntity()).build())
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryBedEntity entity, UpdateResortRoomCategoryBedRequest request) {
        ResortRoomCategoryBedMapper.update(entity, request);
        resortRoomCategoryBedRepository.save(entity);
        log.info("ResortRoomCategoryBed updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryBedEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryBedRepository.save(entity);
        log.info("ResortRoomCategoryBed soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
