package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;

import java.util.List;

public interface ResortRoomCategoryPriceService {

    SuccessResponse create(CreateResortRoomCategoryPriceRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           PriceTypeEntity priceTypeEntity,
                           PriceUnitEntity priceUnitEntity,
                           CurrencyEntity currencyEntity,
                           List<DayOfWeekEntity> dayOfWeekEntities);

    ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity);

    SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                           UpdateResortRoomCategoryPriceRequest request,
                           List<DayOfWeekEntity> dayOfWeekEntities);

    SuccessResponse delete(ResortRoomCategoryPriceEntity entity);
}
