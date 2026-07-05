package com.example.resortbackendapplication1.resortbasicinfo.serviceImpl;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
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

import java.util.Map;

@Service
@Slf4j
public class ResortBasicInfoServiceImpl implements ResortBasicInfoService {

    private static final Long RESORT_BASIC_INFO_ID = 1L;

    private final ResortBasicInfoRepository repository;

    public ResortBasicInfoServiceImpl(ResortBasicInfoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortBasicInfoRequest request,
                                  CountryEntity countryEntity,
                                  CityEntity cityEntity,
                                  Map<Long, LocaleEntity> localeEntityMap) {
        ResortBasicInfoEntity entity = ResortBasicInfoMapper.create(request, countryEntity, cityEntity, localeEntityMap);
        entity.setId(RESORT_BASIC_INFO_ID);
        repository.save(entity);
        log.info("ResortBasicInfo created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortBasicInfoEntity getEntity() {
        return repository.findByIdAndIsActiveAndIsDeleted(RESORT_BASIC_INFO_ID, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortBasicInfo not found"));
    }

    @Override
    public ResortBasicInfoResponse get() {
        ResortBasicInfoEntity entity = getEntity();
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
}
