package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.ResortRoomBedFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.UpdateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroombeds.ResortRoomBedResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomBedDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomBedSearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomBedSortField;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomBedMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomBedRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomBedService;
import com.example.resortbackendapplication1.resort.room.specification.ResortRoomBedSpecification;
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
public class ResortRoomBedServiceImpl implements ResortRoomBedService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomBedSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomBedSearchField.allowedFields();

    private final ResortRoomBedRepository resortRoomBedRepository;

    public ResortRoomBedServiceImpl(ResortRoomBedRepository resortRoomBedRepository) {
        this.resortRoomBedRepository = resortRoomBedRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomBedRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  BedTypeEntity bedTypeEntity) {
        if (resortRoomBedRepository.existsByResortRoomEntity_IdAndBedTypeEntity_IdAndIsActiveAndIsDeleted(
                resortRoomEntity.getId(), bedTypeEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort room already has a bed row for bed type id: "
                    + bedTypeEntity.getId());
        }

        ResortRoomBedEntity entity = ResortRoomBedMapper.create(request);
        resortRoomEntity.addResortRoomBedEntity(entity);
        bedTypeEntity.addResortRoomBedEntity(entity);

        resortRoomBedRepository.save(entity);
        log.info("ResortRoomBed created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomBedEntity getEntityById(Long resortRoomId, Long id) {
        return resortRoomBedRepository
                .findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomBed not found with id: " + id));
    }

    @Override
    public ResortRoomBedResponse getById(Long resortRoomId, Long id) {
        ResortRoomBedEntity entity = getEntityById(resortRoomId, id);
        ResortRoomBedDto dto = ResortRoomBedMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .bedType(BedTypeMapper.toDto(entity.getBedTypeEntity()).build())
                .build();
        return new ResortRoomBedResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomBedDto> getAll(Long resortRoomId, ResortRoomBedFilterRequest request) {
        Specification<@NonNull ResortRoomBedEntity> specification =
                ResortRoomBedSpecification.filter(resortRoomId, request);
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomBedSortField.localeSortFields());
        Page<@NonNull ResortRoomBedDto> page = resortRoomBedRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomBedMapper.toDto(entity)
                        .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                        .bedType(BedTypeMapper.toDto(entity.getBedTypeEntity()).build())
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomBedEntity entity, UpdateResortRoomBedRequest request) {
        ResortRoomBedMapper.update(entity, request);
        resortRoomBedRepository.save(entity);
        log.info("ResortRoomBed updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomBedEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomBedRepository.save(entity);
        log.info("ResortRoomBed soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
