package com.example.resortbackendapplication1.resortroomcategoryprice.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceFilterRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.response.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;

public interface ResortRoomCategoryPriceService {

    SuccessResponse create(CreateResortRoomCategoryPriceRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           PriceTypeEntity priceTypeEntity,
                           PriceUnitEntity priceUnitEntity);

    ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryPriceDto> getAll(ResortRoomCategoryPriceFilterRequest request,
                                                          Long resortRoomCategoryId);

    SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                           UpdateResortRoomCategoryPriceRequest request,
                           PriceTypeEntity priceTypeEntity,
                           PriceUnitEntity priceUnitEntity);

    SuccessResponse delete(ResortRoomCategoryPriceEntity entity);
}
