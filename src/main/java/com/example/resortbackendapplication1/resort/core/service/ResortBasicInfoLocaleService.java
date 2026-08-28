package com.example.resortbackendapplication1.resort.core.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.locale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.locale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.core.dto.response.resortbasicinfolocales.ResortBasicInfoLocaleCountResponse;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoLocaleEntity;

public interface ResortBasicInfoLocaleService {

    SuccessResponse create(CreateResortBasicInfoLocaleRequest request,
                           ResortBasicInfoEntity resortBasicInfoEntity,
                           LocaleEntity localeEntity);

    ResortBasicInfoLocaleEntity getEntityById(Long resortBasicInfoId, Long id);

    PaginatedResponse<ResortBasicInfoLocaleDto> getAll(Long resortBasicInfoId, String localeCode, PaginatedRequest paginatedRequest);

    ResortBasicInfoLocaleCountResponse getActiveCount(Long resortBasicInfoId);

    SuccessResponse update(ResortBasicInfoLocaleEntity entity,
                           UpdateResortBasicInfoLocaleRequest request);

    SuccessResponse delete(ResortBasicInfoLocaleEntity entity);
}
