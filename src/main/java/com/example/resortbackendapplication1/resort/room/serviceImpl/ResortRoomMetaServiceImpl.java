package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroommeta.ResortRoomMetaResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMetaDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMetaMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomMetaRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomMetaService;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ResortRoomMetaServiceImpl implements ResortRoomMetaService {

    private final ResortRoomMetaRepository resortRoomMetaRepository;

    public ResortRoomMetaServiceImpl(ResortRoomMetaRepository resortRoomMetaRepository) {
        this.resortRoomMetaRepository = resortRoomMetaRepository;
    }

    @Override
    public ResortRoomMetaEntity getEntityByResortRoomId(Long resortRoomId) {
        return resortRoomMetaRepository.findByResortRoomEntity_IdAndIsActiveAndIsDeleted(resortRoomId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomMeta not found for resort room id: " + resortRoomId));
    }

    @Override
    public Optional<ResortRoomMetaEntity> findEntityByResortRoomId(Long resortRoomId) {
        return resortRoomMetaRepository.findByResortRoomEntity_IdAndIsActiveAndIsDeleted(resortRoomId, true, false);
    }

    @Override
    public ResortRoomMetaResponse getByResortRoomId(Long resortRoomId, ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback) {
        ResortRoomMetaDto dto = findEntityByResortRoomId(resortRoomId)
                .map(entity -> ResortRoomMetaMapper.toDto(entity).build())
                .orElseGet(() -> ResortRoomMetaMapper.fromCategory(resortRoomCategoryMetaFallback));
        return new ResortRoomMetaResponse(dto);
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomMetaRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  UnitEntity roomSizeUnitEntity) {
        if (findEntityByResortRoomId(resortRoomEntity.getId()).isPresent()) {
            throw new IllegalStateException(
                    "This room already has an active meta override for resort room id: " + resortRoomEntity.getId());
        }
        ResortRoomMetaEntity entity = ResortRoomMetaMapper.create(request, roomSizeUnitEntity);
        resortRoomEntity.assignResortRoomMetaEntity(entity);
        resortRoomMetaRepository.save(entity);
        log.info("ResortRoomMeta created for resort room id: {}, id: {}", resortRoomEntity.getId(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomMetaEntity entity,
                                  UpdateResortRoomMetaRequest request,
                                  UnitEntity roomSizeUnitEntity) {
        ResortRoomMetaMapper.update(entity, request, roomSizeUnitEntity);
        resortRoomMetaRepository.save(entity);
        log.info("ResortRoomMeta updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomMetaEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomMetaRepository.save(entity);
        log.info("ResortRoomMeta soft-deleted with id: {} — resort room now inherits from its category", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
