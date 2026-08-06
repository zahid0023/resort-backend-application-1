package com.example.resortbackendapplication1.roomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.roomcategory.model.mapper.RoomCategoryLocaleMapper;
import com.example.resortbackendapplication1.roomcategory.repository.RoomCategoryLocaleRepository;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryLocaleService;
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
public class RoomCategoryLocaleServiceImpl implements RoomCategoryLocaleService {
    private final RoomCategoryLocaleRepository roomCategoryLocaleRepository;

    public RoomCategoryLocaleServiceImpl(RoomCategoryLocaleRepository roomCategoryLocaleRepository) {
        this.roomCategoryLocaleRepository = roomCategoryLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateRoomCategoryLocaleRequest request,
                                  RoomCategoryEntity roomCategoryEntity,
                                  LocaleEntity localeEntity) {
        if (roomCategoryLocaleRepository.existsByRoomCategoryEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                roomCategoryEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("RoomCategoryLocale already exists for roomCategoryId '"
                    + roomCategoryEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (roomCategoryLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("RoomCategoryLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        RoomCategoryLocaleEntity entity = RoomCategoryLocaleMapper.create(request);
        roomCategoryEntity.addRoomCategoryLocaleEntity(entity);
        localeEntity.addRoomCategoryLocaleEntity(entity);
        roomCategoryLocaleRepository.save(entity);
        log.info("RoomCategoryLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(RoomCategoryLocaleEntity entity,
                                  UpdateRoomCategoryLocaleRequest request) {
        if (roomCategoryLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("RoomCategoryLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        RoomCategoryLocaleMapper.update(entity, request);
        roomCategoryLocaleRepository.save(entity);
        log.info("RoomCategoryLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(RoomCategoryLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomCategoryLocaleRepository.save(entity);
        log.info("RoomCategoryLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomCategoryLocaleEntity getEntityById(Long roomCategoryId, Long id) {
        return roomCategoryLocaleRepository
                .findByRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(roomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("RoomCategoryLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<RoomCategoryLocaleDto> getAll(Long roomCategoryId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull RoomCategoryLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? roomCategoryLocaleRepository.findByRoomCategoryEntity_IdAndIsActiveAndIsDeleted(roomCategoryId, true, false, pageable)
                : roomCategoryLocaleRepository.findByRoomCategoryEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        roomCategoryId, localeCode, true, false, pageable))
                .map(RoomCategoryLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long roomCategoryId) {
        List<String> codes = roomCategoryLocaleRepository
                .findLocaleEntity_CodeByRoomCategoryEntity_IdAndIsActiveAndIsDeleted(roomCategoryId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
