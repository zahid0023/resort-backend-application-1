package com.example.resortbackendapplication1.roomstatus.serviceImpl;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.CreateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.UpdateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusLocaleDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.roomstatus.model.mapper.RoomStatusLocaleMapper;
import com.example.resortbackendapplication1.roomstatus.repository.RoomStatusLocaleRepository;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusLocaleService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class RoomStatusLocaleServiceImpl implements RoomStatusLocaleService {
    private final RoomStatusLocaleRepository roomStatusLocaleRepository;

    public RoomStatusLocaleServiceImpl(RoomStatusLocaleRepository roomStatusLocaleRepository) {
        this.roomStatusLocaleRepository = roomStatusLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateRoomStatusLocaleRequest request,
                                  RoomStatusEntity roomStatusEntity,
                                  LocaleEntity localeEntity) {
        if (roomStatusLocaleRepository.existsByRoomStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                roomStatusEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("RoomStatus already has a locale entry for locale id: " + localeEntity.getId());
        }

        RoomStatusLocaleEntity entity = RoomStatusLocaleMapper.create(request);
        roomStatusEntity.addRoomStatusLocaleEntity(entity);
        localeEntity.addRoomStatusLocaleEntity(entity);
        roomStatusLocaleRepository.save(entity);
        log.info("RoomStatusLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(RoomStatusLocaleEntity entity,
                                  UpdateRoomStatusLocaleRequest request) {
        RoomStatusLocaleMapper.update(entity, request);
        roomStatusLocaleRepository.save(entity);
        log.info("RoomStatusLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(RoomStatusLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomStatusLocaleRepository.save(entity);
        log.info("RoomStatusLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomStatusLocaleEntity getEntityById(Long roomStatusId, Long id) {
        return roomStatusLocaleRepository
                .findByRoomStatusEntity_IdAndIdAndIsActiveAndIsDeleted(roomStatusId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("RoomStatusLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<RoomStatusLocaleDto> getAll(Long roomStatusId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull RoomStatusLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? roomStatusLocaleRepository.findByRoomStatusEntity_IdAndIsActiveAndIsDeleted(roomStatusId, true, false, pageable)
                : roomStatusLocaleRepository.findByRoomStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        roomStatusId, localeCode, true, false, pageable))
                .map(RoomStatusLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long roomStatusId) {
        List<String> codes = roomStatusLocaleRepository
                .findLocaleEntity_CodeByRoomStatusEntity_IdAndIsActiveAndIsDeleted(roomStatusId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
