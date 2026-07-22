package com.example.resortbackendapplication1.bedtype.service;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.BedTypeFilterRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.CreateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.UpdateBedTypeRequest;
import com.example.resortbackendapplication1.bedtype.dto.response.bedtypes.BedTypeResponse;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BedTypeService {

    SuccessResponse create(CreateBedTypeRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    BedTypeEntity getEntityById(Long id);

    BedTypeResponse getById(Long id);

    PaginatedResponse<BedTypeDto> getAll(BedTypeFilterRequest request);

    SuccessResponse update(BedTypeEntity entity,
                           UpdateBedTypeRequest request);

    SuccessResponse delete(Long id);

    List<BedTypeEntity> getAll(Set<Long> ids);
}
