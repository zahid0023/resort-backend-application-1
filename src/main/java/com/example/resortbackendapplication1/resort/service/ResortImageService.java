package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.imagehosting.dto.request.ImageRequest;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortimage.UpdateResortImageRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortImageResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortImageEntity;
import com.example.resortbackendapplication1.resort.model.projection.ResortImageSummary;

import java.util.List;

public interface ResortImageService {

    List<ResortImageDto> createAll(List<ImageRequest> imageRequests, ResortImageHostingConfigEntity config);

    ResortImageEntity getEntityById(Long id);

    ResortImageResponse getById(Long id);

    PaginatedResponse<ResortImageSummary> getAll(Long resortId, PaginatedRequest request);

    SuccessResponse update(ResortImageEntity entity, UpdateResortImageRequest request);

    SuccessResponse delete(ResortImageEntity entity);
}
