package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;

import java.util.List;
import java.util.Set;

public interface ResortService {

    SuccessResponse create(CreateResortRequest request);

    ResortEntity getEntityById(Long id);

    ResortResponse getById(Long id);

    PaginatedResponse<ResortDto> getAll(ResortFilterRequest request);

    PaginatedResponse<ResortDto> getAllForUser(ResortFilterRequest request, Long userId);

    SuccessResponse update(ResortEntity entity, UpdateResortRequest request);

    SuccessResponse delete(Long id);

    List<ResortEntity> getAll(Set<Long> ids);
}
