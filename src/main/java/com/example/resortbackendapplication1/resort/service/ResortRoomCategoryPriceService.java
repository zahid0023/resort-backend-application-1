package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.CreateResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;

import java.util.List;

public interface ResortRoomCategoryPriceService {

    /**
     * Adds a new currency's full BASE/WEEKDAY/WEEKEND price set to an already-existing resort room category —
     * the same bundling {@link com.example.resortbackendapplication1.resort.serviceImpl.ResortRoomCategoryServiceImpl}
     * does per currency at creation time, exposed here so additional currencies can be added afterward.
     */
    CreateResortRoomCategoryPriceGroupResponse createMain(CreateResortRoomCategoryPriceGroupRequest request,
                                                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                           PriceTypeEntity basePriceTypeEntity,
                                                           PriceTypeEntity weekdayPriceTypeEntity,
                                                           PriceTypeEntity weekendPriceTypeEntity,
                                                           CurrencyEntity currencyEntity,
                                                           PriceUnitEntity basePriceUnitEntity,
                                                           PriceUnitEntity weekdayPriceUnitEntity,
                                                           PriceUnitEntity weekendPriceUnitEntity,
                                                           List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                                           List<DayOfWeekEntity> weekendDayOfWeekEntities);

    SuccessResponse createHoliday(CreateResortRoomCategoryHolidayPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  PriceTypeEntity holidayPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity);

    SuccessResponse createSpecial(CreateResortRoomCategorySpecialPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  PriceTypeEntity specialPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity);

    ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity);

    SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                           UpdateResortRoomCategoryPriceRequest request,
                           List<DayOfWeekEntity> dayOfWeekEntities);

    SuccessResponse delete(ResortRoomCategoryPriceEntity entity);
}
