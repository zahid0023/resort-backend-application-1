package com.example.resortbackendapplication1.unittype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.CreateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UnitTypeFilterRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.UpdateUnitTypeRequest;
import com.example.resortbackendapplication1.unittype.dto.response.unittypes.UnitTypeResponse;
import com.example.resortbackendapplication1.unittype.model.dto.UnitTypeDto;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UnitTypeService {

    SuccessResponse create(CreateUnitTypeRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    UnitTypeEntity getEntityById(Long id);

    UnitTypeResponse getById(Long id);

    PaginatedResponse<UnitTypeDto> getAll(UnitTypeFilterRequest request);

    SuccessResponse update(UnitTypeEntity entity,
                           UpdateUnitTypeRequest request);

    SuccessResponse delete(Long id);

    List<UnitTypeEntity> getAll(Set<Long> ids);
}
