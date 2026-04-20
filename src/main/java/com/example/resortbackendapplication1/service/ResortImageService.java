package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.response.resortimages.ResortImageResponse;
import com.example.resortbackendapplication1.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResortImageService {

    SuccessResponse saveImages(ResortEntity resortEntity, List<ImageRequest> imageRequests);

    ResortImageEntity getResortImageEntity(Long resortId, Long id);

    ResortImageResponse getResortImage(Long resortId, Long id);

    PaginatedResponse<ResortImageDto> getAllResortImages(Long resortId, Pageable pageable);

    ResortImageResponse updateResortImage(ResortImageEntity entity, List<ImageRequest> imageRequests);

    SuccessResponse deleteResortImage(Long resortId, Long id);
}
