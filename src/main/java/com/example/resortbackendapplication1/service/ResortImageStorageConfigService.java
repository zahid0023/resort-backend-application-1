package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.UpdateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.response.resortimagestorageconfigs.ResortImageStorageConfigResponse;
import com.example.resortbackendapplication1.model.dto.ResortImageStorageConfigDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageStorageConfigEntity;
import org.springframework.data.domain.Pageable;

public interface ResortImageStorageConfigService {

    SuccessResponse createResortImageStorageConfig(CreateResortImageStorageConfigRequest request,
                                                   ResortEntity resortEntity);

    ResortImageStorageConfigEntity getResortImageStorageConfigEntity(Long resortId, Long id);

    ResortImageStorageConfigResponse getResortImageStorageConfig(Long resortId, Long id);

    ResortImageStorageConfigResponse getResortImageStorageConfig(Long resortId);

    PaginatedResponse<ResortImageStorageConfigDto> getAllResortImageStorageConfigs(Long resortId, Pageable pageable);

    SuccessResponse updateResortImageStorageConfig(ResortImageStorageConfigEntity entity,
                                                   UpdateResortImageStorageConfigRequest request);

    SuccessResponse deleteResortImageStorageConfig(Long resortId, Long id);
}
