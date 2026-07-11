package com.example.resortbackendapplication1.resortbasicinfo.service;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.response.ResortBasicInfoResponse;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;

import java.util.Map;

public interface ResortBasicInfoService {

    SuccessResponse create(CreateResortBasicInfoRequest request,
                           ResortEntity resortEntity,
                           CountryEntity countryEntity,
                           CityEntity cityEntity,
                           Map<Long, LocaleEntity> localeEntityMap);

    ResortBasicInfoEntity getEntityByResortId(Long resortId);

    ResortBasicInfoResponse getByResortId(Long resortId);

    SuccessResponse update(ResortBasicInfoEntity entity,
                           UpdateResortBasicInfoRequest request,
                           CountryEntity countryEntity,
                           CityEntity cityEntity);
}
