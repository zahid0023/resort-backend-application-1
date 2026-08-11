package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;

public interface PriceUnitScopeAssignmentService {

    SuccessResponse assign(PriceUnitEntity priceUnitEntity,
                           PriceScopeEntity priceScopeEntity);

    PriceUnitScopeAssignmentEntity getEntityByPriceScopeId(Long priceUnitId, Long priceScopeId);

    SuccessResponse unassign(PriceUnitScopeAssignmentEntity entity);
}
