package com.example.resortbackendapplication1.resortbasicinfo.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;

public interface ResortBasicInfoLocaleService {

    SuccessResponse create(ResortBasicInfoEntity resortBasicInfoEntity,
                           LocaleEntity localeEntity,
                           CreateResortBasicInfoLocaleRequest request);

    ResortBasicInfoLocaleEntity getEntityById(Long resortBasicInfoId, Long id);

    SuccessResponse update(ResortBasicInfoLocaleEntity entity, UpdateResortBasicInfoLocaleRequest request);

    SuccessResponse delete(ResortBasicInfoLocaleEntity entity);
}
