package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortaccesstypes.CreateResortAccessTypeRequest;
import com.example.resortbackendapplication1.dto.request.resortaccesstypes.UpdateResortAccessTypeRequest;
import com.example.resortbackendapplication1.dto.response.resortaccesstypes.ResortAccessTypeResponse;
import com.example.resortbackendapplication1.model.dto.ResortAccessTypeDto;
import com.example.resortbackendapplication1.model.entity.ResortAccessTypeEntity;
import org.springframework.data.domain.Pageable;

public interface ResortAccessTypeService {
    SuccessResponse createResortAccessType(CreateResortAccessTypeRequest request);

    ResortAccessTypeEntity getResortAccessTypeEntity(Long id);

    ResortAccessTypeEntity getResortAccessTypeByCode(String code);

    ResortAccessTypeResponse getResortAccessType(Long id);

    PaginatedResponse<ResortAccessTypeDto> getAllResortAccessTypes(Pageable pageable);

    SuccessResponse updateResortAccessType(Long id, UpdateResortAccessTypeRequest request);

    SuccessResponse deleteResortAccessType(Long id);
}
