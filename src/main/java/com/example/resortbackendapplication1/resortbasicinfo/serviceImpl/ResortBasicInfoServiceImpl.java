package com.example.resortbackendapplication1.resortbasicinfo.serviceImpl;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.imagehosting.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.response.ResortBasicInfoResponse;
import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoDto;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.mapper.ResortBasicInfoMapper;
import com.example.resortbackendapplication1.resortbasicinfo.repository.ResortBasicInfoRepository;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortBasicInfoServiceImpl implements ResortBasicInfoService {

    private final ResortBasicInfoRepository repository;

    public ResortBasicInfoServiceImpl(ResortBasicInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResortBasicInfoEntity getEntityByResortId(Long resortId) {
        return repository.findByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortBasicInfo not found for resort with id: " + resortId));
    }

    @Override
    public ResortBasicInfoResponse getByResortId(Long resortId) {
        ResortBasicInfoEntity entity = getEntityByResortId(resortId);
        ResortBasicInfoDto dto = ResortBasicInfoMapper.toDto(entity);
        return new ResortBasicInfoResponse(dto);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortBasicInfoEntity entity,
                                  UpdateResortBasicInfoRequest request,
                                  CountryEntity countryEntity,
                                  CityEntity cityEntity) {
        ResortBasicInfoMapper.update(entity, request, countryEntity, cityEntity);
        repository.save(entity);
        log.info("ResortBasicInfo updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse uploadLogo(ResortBasicInfoEntity entity, ImageUploadResponse imageUploadResponse) {
        entity.setLogoUrl(imageUploadResponse.getImageUrl());
        repository.save(entity);
        log.info("ResortBasicInfo logo uploaded, url: {}", imageUploadResponse.getImageUrl());
        return new SuccessResponse(true, entity.getId());
    }
}
