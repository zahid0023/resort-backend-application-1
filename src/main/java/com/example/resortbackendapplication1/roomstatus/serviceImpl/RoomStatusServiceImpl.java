package com.example.resortbackendapplication1.roomstatus.serviceImpl;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.RoomStatusFilterRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.CreateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.UpdateRoomStatusRequest;
import com.example.resortbackendapplication1.roomstatus.dto.response.roomstatuses.RoomStatusResponse;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.roomstatus.model.enums.RoomStatusSearchField;
import com.example.resortbackendapplication1.roomstatus.model.enums.RoomStatusSortField;
import com.example.resortbackendapplication1.roomstatus.model.mapper.RoomStatusLocaleMapper;
import com.example.resortbackendapplication1.roomstatus.model.mapper.RoomStatusMapper;
import com.example.resortbackendapplication1.roomstatus.repository.RoomStatusRepository;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusService;
import com.example.resortbackendapplication1.roomstatus.specification.RoomStatusSpecification;
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
public class RoomStatusServiceImpl implements RoomStatusService {

    private static final Set<String> ALLOWED_SORT_FIELDS = RoomStatusSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = RoomStatusSearchField.allowedFields();

    private final RoomStatusRepository roomStatusRepository;

    public RoomStatusServiceImpl(RoomStatusRepository roomStatusRepository) {
        this.roomStatusRepository = roomStatusRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateRoomStatusRequest request, LocaleEntity localeEntity) {
        if (roomStatusRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("RoomStatus with code '" + request.getCode() + "' already exists");
        }

        RoomStatusEntity entity = RoomStatusMapper.create(request);

        RoomStatusLocaleEntity roomStatusLocaleEntity = RoomStatusLocaleMapper.create(request.getLocale());
        localeEntity.addRoomStatusLocaleEntity(roomStatusLocaleEntity);

        entity.addRoomStatusLocaleEntity(roomStatusLocaleEntity);

        roomStatusRepository.save(entity);
        log.info("RoomStatus created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomStatusEntity getEntityById(Long id) {
        return roomStatusRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("RoomStatus not found with id: " + id));
    }

    @Override
    public RoomStatusResponse getById(Long id) {
        RoomStatusEntity entity = getEntityById(id);
        RoomStatusDto dto = RoomStatusMapper.toDto(entity).build();
        return new RoomStatusResponse(dto);
    }

    @Override
    public PaginatedResponse<RoomStatusDto> getAll(RoomStatusFilterRequest request) {
        Specification<@NonNull RoomStatusEntity> specification =
                RoomStatusSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, RoomStatusSortField.localeSortFields());
        Page<@NonNull RoomStatusDto> page = roomStatusRepository
                .findAll(specification, pageable)
                .map(entity -> RoomStatusMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(RoomStatusEntity entity, UpdateRoomStatusRequest request) {
        RoomStatusMapper.update(entity, request);
        roomStatusRepository.save(entity);
        log.info("RoomStatus updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(RoomStatusEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getRoomStatusLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        roomStatusRepository.save(entity);
        log.info("RoomStatus soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
