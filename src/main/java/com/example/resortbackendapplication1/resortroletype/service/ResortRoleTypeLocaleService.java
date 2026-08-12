package com.example.resortbackendapplication1.resortroletype.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.CreateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.UpdateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeLocaleDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;

public interface ResortRoleTypeLocaleService {
    SuccessResponse create(CreateResortRoleTypeLocaleRequest request,
                           ResortRoleTypeEntity resortRoleTypeEntity,
                           LocaleEntity localeEntity);

    ResortRoleTypeLocaleEntity getEntityById(Long resortRoleTypeId, Long id);

    PaginatedResponse<ResortRoleTypeLocaleDto> getAll(Long resortRoleTypeId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(ResortRoleTypeLocaleEntity entity,
                           UpdateResortRoleTypeLocaleRequest request);

    SuccessResponse delete(ResortRoleTypeLocaleEntity entity);
}
