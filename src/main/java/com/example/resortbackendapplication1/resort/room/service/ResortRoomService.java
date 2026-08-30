package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortrooms.ResortRoomResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

import java.util.List;

public interface ResortRoomService {

    SuccessResponse create(CreateResortRoomRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           RoomStatusEntity roomStatusEntity,
                           LocaleEntity localeEntity,
                           UnitEntity roomSizeUnitEntity,
                           List<BedTypeEntity> bedTypeEntities,
                           List<CurrencyEntity> currencyEntities,
                           List<PriceUnitEntity> priceUnitEntities);

    ResortRoomEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomResponse getById(Long resortRoomCategoryId, Long id,
                               ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                               List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback);

    PaginatedResponse<ResortRoomDto> getAll(Long resortRoomCategoryId, ResortRoomFilterRequest request,
                                            ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                                            List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback);

    /**
     * The exact own-vs-inherited-from-category meta/beds resolution getById/getAll use, exposed here so
     * another controller with its own already-fetched entity (e.g. AvailabilityController, which spans rooms
     * across every category in a resort rather than one category) can reuse it instead of duplicating it.
     */
    ResortRoomDto buildDto(ResortRoomEntity entity,
                           ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                           List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback);

    SuccessResponse update(ResortRoomEntity entity, UpdateResortRoomRequest request);

    /** Room status transitions are deliberately kept out of {@link #update}; this is their only entry point. */
    SuccessResponse updateStatus(ResortRoomEntity entity, RoomStatusEntity roomStatusEntity);

    SuccessResponse delete(ResortRoomEntity entity);
}
