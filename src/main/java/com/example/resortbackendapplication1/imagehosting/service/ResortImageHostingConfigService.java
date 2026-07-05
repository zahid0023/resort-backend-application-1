package com.example.resortbackendapplication1.imagehosting.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.response.ResortImageHostingConfigResponse;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.model.projection.ResortImageHostingConfigSummary;

public interface ResortImageHostingConfigService {

    SuccessResponse create(CreateResortImageHostingConfigRequest request);

    ResortImageHostingConfigEntity getEntityById(Long id);

    ResortImageHostingConfigResponse getById(Long id);

    PaginatedResponse<ResortImageHostingConfigSummary> getAll(PaginatedRequest request);

    SuccessResponse update(ResortImageHostingConfigEntity entity, UpdateResortImageHostingConfigRequest request);

    SuccessResponse delete(Long id);
}
