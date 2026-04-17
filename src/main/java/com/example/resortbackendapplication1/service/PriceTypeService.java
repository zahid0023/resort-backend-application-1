package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.pricetypes.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.request.pricetypes.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface PriceTypeService {
    SuccessResponse createPriceType(CreatePriceTypeRequest request);
    PriceTypeEntity getPriceTypeEntity(Long id);
    PriceTypeResponse getPriceType(Long id);
    PaginatedResponse<PriceTypeDto> getAllPriceTypes(Pageable pageable);
    SuccessResponse updatePriceType(Long id, UpdatePriceTypeRequest request);
    SuccessResponse deletePriceType(Long id);
}
