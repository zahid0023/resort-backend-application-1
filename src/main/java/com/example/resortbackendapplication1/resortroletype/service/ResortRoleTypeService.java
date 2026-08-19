package com.example.resortbackendapplication1.resortroletype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.CreateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeFilterRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.UpdateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.response.resortroletypes.ResortRoleTypeResponse;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;

public interface ResortRoleTypeService {

    SuccessResponse create(CreateResortRoleTypeRequest request,
                           LocaleEntity localeEntity);

    ResortRoleTypeEntity getEntityById(Long id);

    ResortRoleTypeEntity getEntityByCode(String code);

    ResortRoleTypeResponse getById(Long id);

    PaginatedResponse<ResortRoleTypeDto> getAll(ResortRoleTypeFilterRequest request);

    SuccessResponse update(ResortRoleTypeEntity entity,
                           UpdateResortRoleTypeRequest request);

    SuccessResponse delete(ResortRoleTypeEntity entity);
}
