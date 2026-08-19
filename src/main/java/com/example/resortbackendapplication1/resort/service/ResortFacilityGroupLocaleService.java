package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.locale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.locale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;

public interface ResortFacilityGroupLocaleService {

    SuccessResponse create(CreateResortFacilityGroupLocaleRequest request,
                           ResortFacilityGroupEntity resortFacilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortFacilityGroupLocaleEntity getEntityById(Long resortFacilityGroupId, Long id);

    PaginatedResponse<ResortFacilityGroupLocaleDto> getAll(Long resortFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(ResortFacilityGroupLocaleEntity entity,
                           UpdateResortFacilityGroupLocaleRequest request);

    SuccessResponse delete(ResortFacilityGroupLocaleEntity entity);
}
