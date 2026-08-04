package com.example.resortbackendapplication1.image.hosting.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.image.hosting.dto.request.imagehostingprovider.CreateImageHostingProviderRequest;
import com.example.resortbackendapplication1.image.hosting.dto.request.imagehostingprovider.ImageHostingProviderFilterRequest;
import com.example.resortbackendapplication1.image.hosting.dto.request.imagehostingprovider.UpdateImageHostingProviderRequest;
import com.example.resortbackendapplication1.image.hosting.dto.response.imagehostingproviders.ImageHostingProviderResponse;
import com.example.resortbackendapplication1.image.hosting.model.dto.ImageHostingProviderDto;
import com.example.resortbackendapplication1.image.hosting.model.entity.ImageHostingProviderEntity;

public interface ImageHostingProviderService {

    SuccessResponse create(CreateImageHostingProviderRequest request);

    ImageHostingProviderEntity getEntityById(Long id);

    ImageHostingProviderResponse getById(Long id);

    PaginatedResponse<ImageHostingProviderDto> getAll(ImageHostingProviderFilterRequest request);

    SuccessResponse update(ImageHostingProviderEntity entity, UpdateImageHostingProviderRequest request);

    SuccessResponse delete(ImageHostingProviderEntity entity);
}
