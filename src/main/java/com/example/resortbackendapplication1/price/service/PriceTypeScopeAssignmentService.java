package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeAssignmentDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;

public interface PriceTypeScopeAssignmentService {

    SuccessResponse assign(PriceTypeScopeEntity priceTypeScopeEntity,
                           PriceTypeEntity priceTypeEntity);

    PriceTypeScopeAssignmentEntity getEntityById(Long priceTypeScopeId, Long id);

    PaginatedResponse<PriceTypeScopeAssignmentDto> getAll(Long priceTypeScopeId, PaginatedRequest paginatedRequest);

    SuccessResponse unassign(PriceTypeScopeAssignmentEntity entity);
}
