package com.example.resortbackendapplication1.resortroomcategory.serviceImpl;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.mapper.ResortRoomCategoryBedMapper;
import com.example.resortbackendapplication1.resortroomcategory.repository.ResortRoomCategoryBedRepository;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryBedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortRoomCategoryBedServiceImpl implements ResortRoomCategoryBedService {

    private final ResortRoomCategoryBedRepository resortRoomCategoryBedRepository;

    public ResortRoomCategoryBedServiceImpl(ResortRoomCategoryBedRepository resortRoomCategoryBedRepository) {
        this.resortRoomCategoryBedRepository = resortRoomCategoryBedRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                   BedTypeEntity bedTypeEntity,
                                   ResortRoomCategoryBedRequest request) {
        ResortRoomCategoryBedEntity entity = ResortRoomCategoryBedMapper.create(request, resortRoomCategoryEntity, bedTypeEntity);
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
