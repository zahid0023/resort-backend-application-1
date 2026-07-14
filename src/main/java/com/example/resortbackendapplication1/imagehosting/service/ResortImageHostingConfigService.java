package com.example.resortbackendapplication1.imagehosting.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.response.ResortImageHostingConfigResponse;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.model.projection.ResortImageHostingConfigSummary;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;

public interface ResortImageHostingConfigService {

    SuccessResponse create(CreateResortImageHostingConfigRequest request, ResortEntity resortEntity);

    ResortImageHostingConfigEntity getEntityById(Long resortId, Long id);

    ResortImageHostingConfigResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortImageHostingConfigSummary> getAll(Long resortId, PaginatedRequest request);

    SuccessResponse update(ResortImageHostingConfigEntity entity, UpdateResortImageHostingConfigRequest request);

    SuccessResponse delete(Long resortId, Long id);
}
