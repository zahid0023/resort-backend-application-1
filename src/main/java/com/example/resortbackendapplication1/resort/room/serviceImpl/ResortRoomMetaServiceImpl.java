package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMetaMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomMetaRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomMetaService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
