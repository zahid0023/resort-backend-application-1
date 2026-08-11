package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;

public interface PriceTypeScopeAssignmentService {

    SuccessResponse assign(PriceTypeEntity priceTypeEntity,
                           PriceScopeEntity priceScopeEntity);

    PriceTypeScopeAssignmentEntity getEntityByPriceScopeId(Long priceTypeId, Long priceScopeId);

    SuccessResponse unassign(PriceTypeScopeAssignmentEntity entity);
}
