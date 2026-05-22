package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.model.mapper.RoomCategoryLocaleMapper;
import com.example.resortbackendapplication1.repository.RoomCategoryLocaleRepository;
import com.example.resortbackendapplication1.service.RoomCategoryLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RoomCategoryLocaleServiceImpl implements RoomCategoryLocaleService {

    private final RoomCategoryLocaleRepository roomCategoryLocaleRepository;

    public RoomCategoryLocaleServiceImpl(RoomCategoryLocaleRepository roomCategoryLocaleRepository) {
        this.roomCategoryLocaleRepository = roomCategoryLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(RoomCategoryEntity roomCategoryEntity,
                                  LocaleEntity localeEntity,
                                  CreateRoomCategoryLocaleRequest request) {
        RoomCategoryLocaleEntity entity = RoomCategoryLocaleMapper.create(request, roomCategoryEntity, localeEntity);
        roomCategoryLocaleRepository.save(entity);
        log.info("RoomCategoryLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomCategoryLocaleEntity getEntityById(Long roomCategoryId, Long id) {
        return roomCategoryLocaleRepository
                .findByRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(roomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("RoomCategoryLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(RoomCategoryLocaleEntity entity,
                                  UpdateRoomCategoryLocaleRequest request) {
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
}
