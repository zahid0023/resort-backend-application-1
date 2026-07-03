package com.example.resortbackendapplication1.uiblocksection.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.CreateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionFilterRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UpdateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.response.uiblocksections.UiBlockSectionResponse;
import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionDto;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UiBlockSectionService {

    SuccessResponse create(CreateUiBlockSectionRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    UiBlockSectionEntity getEntityById(Long id);

    UiBlockSectionResponse getById(Long id);

    PaginatedResponse<UiBlockSectionDto> getAll(UiBlockSectionFilterRequest request);

    SuccessResponse update(UiBlockSectionEntity entity,
                           UpdateUiBlockSectionRequest request);

    SuccessResponse delete(Long id);

    List<UiBlockSectionEntity> getAll(Set<Long> ids);
}
