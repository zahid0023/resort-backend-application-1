package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortBasicInfoMapper;
import com.example.resortbackendapplication1.resort.repository.ResortBasicInfoRepository;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortBasicInfoServiceImpl implements ResortBasicInfoService {

    private final ResortBasicInfoRepository resortBasicInfoRepository;

    public ResortBasicInfoServiceImpl(ResortBasicInfoRepository resortBasicInfoRepository) {
        this.resortBasicInfoRepository = resortBasicInfoRepository;
    }

    @Override
    public ResortBasicInfoEntity getEntityByResortId(Long resortId) {
        return resortBasicInfoRepository.findByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortBasicInfo not found for resort id: " + resortId));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortBasicInfoEntity entity, UpdateResortBasicInfoRequest request) {
        ResortBasicInfoMapper.update(entity, request);
        resortBasicInfoRepository.save(entity);
        log.info("ResortBasicInfo updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
