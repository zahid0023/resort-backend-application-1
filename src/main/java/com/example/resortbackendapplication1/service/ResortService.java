package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.dto.response.resorts.ResortResponse;
import com.example.resortbackendapplication1.model.dto.ResortDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import org.springframework.data.domain.Pageable;

public interface ResortService {
    SuccessResponse createResort(CreateResortRequest request);

    ResortEntity getResortById(Long id);

    ResortResponse getResort(Long id);

    PaginatedResponse<ResortDto> getAllResorts(Pageable pageable);

    ResortResponse updateResort(Long id, UpdateResortRequest request);

    SuccessResponse deleteResort(Long id);
}
