package com.example.resortbackendapplication1.resortroomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.meta.ResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resortroomcategory.repository.ResortRoomCategoryMetaRepository;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryMetaService;
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
        return resortRoomCategoryMetaRepository
                .findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ResortRoomCategoryMeta not found for room category id: " + resortRoomCategoryId));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryMetaEntity entity,
                                  ResortRoomCategoryMetaRequest request,
                                  UnitEntity roomSizeUnit) {
        ResortRoomCategoryMetaMapper.update(entity, request, roomSizeUnit);
        resortRoomCategoryMetaRepository.save(entity);
        log.info("ResortRoomCategoryMeta updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
