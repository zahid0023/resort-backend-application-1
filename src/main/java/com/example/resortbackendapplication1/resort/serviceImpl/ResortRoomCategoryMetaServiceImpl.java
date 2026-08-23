package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.UpdateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryMetaRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortRoomCategoryMetaServiceImpl implements ResortRoomCategoryMetaService {

    private final ResortRoomCategoryMetaRepository resortRoomCategoryMetaRepository;

    public ResortRoomCategoryMetaServiceImpl(ResortRoomCategoryMetaRepository resortRoomCategoryMetaRepository) {
        this.resortRoomCategoryMetaRepository = resortRoomCategoryMetaRepository;
    }

    @Override
    public ResortRoomCategoryMetaEntity getEntityByResortRoomCategoryId(Long resortRoomCategoryId) {
        return resortRoomCategoryMetaRepository.findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryMeta not found for resort room category id: " + resortRoomCategoryId));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryMetaEntity entity,
                                  UpdateResortRoomCategoryMetaRequest request,
                                  UnitEntity roomSizeUnitEntity) {
        ResortRoomCategoryMetaMapper.update(entity, request, roomSizeUnitEntity);
        resortRoomCategoryMetaRepository.save(entity);
        log.info("ResortRoomCategoryMeta updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
