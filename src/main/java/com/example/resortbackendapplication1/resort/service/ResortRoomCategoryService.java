package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategories.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

import java.util.List;

public interface ResortRoomCategoryService {

    SuccessResponse create(CreateResortRoomCategoryRequest request,
                           ResortEntity resortEntity,
                           RoomCategoryEntity roomCategoryEntity,
                           LocaleEntity localeEntity,
                           UnitEntity roomSizeUnitEntity,
                           List<BedTypeEntity> bedTypeEntities,
                           PriceTypeEntity basePriceTypeEntity,
                           PriceTypeEntity weekdayPriceTypeEntity,
                           PriceTypeEntity weekendPriceTypeEntity,
                           List<CurrencyEntity> currencyEntities,
                           List<PriceUnitEntity> priceUnitEntities,
                           List<DayOfWeekEntity> dayOfWeekEntities);

    ResortRoomCategoryEntity getEntityById(Long resortId, Long id);

    ResortRoomCategoryResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortRoomCategoryDto> getAll(Long resortId, ResortRoomCategoryFilterRequest request);

    SuccessResponse update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request);

    SuccessResponse delete(ResortRoomCategoryEntity entity);
}
