package com.example.resortbackendapplication1.resort.availability.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.resort.availability.dto.request.availability.AvailabilityRequest;
import com.example.resortbackendapplication1.resort.availability.dto.response.availability.AvailabilityResponse;
import com.example.resortbackendapplication1.resort.availability.model.dto.AvailableRoomDto;
import com.example.resortbackendapplication1.resort.availability.model.mapper.AvailableRoomPriceMapper;
import com.example.resortbackendapplication1.resort.availability.service.AvailabilityService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryMetaService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The single availability check both the POS (booker) flow and, later, the website/OTA booking flow call —
 * there is deliberately no separate "manual booking availability" logic anywhere. Each returned room is the
 * exact same ResortRoomDto shape (own-vs-inherited meta/beds included) that GET .../rooms (List Resort Rooms)
 * returns — assembled here via ResortRoomService#buildDto per distinct category among the available rooms,
 * since a ServiceImpl must never call ResortRoomCategoryMetaService/ResortRoomCategoryBedService itself.
 *
 * <p>Availability itself (AvailabilityService) knows nothing about pricing — it only ever answers "which rooms
 * are free for this date range." Pricing each available room for the requested currency, night by night, is
 * entirely {@link RoomPricingResolver}'s job (the same resolver {@code ResortBookingController} uses to compute
 * {@code total_price} at reservation-creation time, so the figure quoted here can never drift from what
 * actually gets charged) — this controller only orchestrates the call and, via
 * {@link AvailableRoomPriceMapper}, its DTO shape. Whether a room counts as "unpriceable" for the requested
 * currency is pricing's decision, not this controller's: {@code tryResolveNightly} reports it as an empty
 * result rather than throwing, and such rooms are simply excluded from the response, the same way a room with
 * an overlapping reservation is excluded.
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final ResortService resortService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final ResortRoomCategoryBedService resortRoomCategoryBedService;
    private final RoomPricingResolver roomPricingResolver;
    private final CurrencyService currencyService;

    public AvailabilityController(AvailabilityService availabilityService,
                                  ResortService resortService,
                                  ResortRoomService resortRoomService,
                                  ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                  ResortRoomCategoryBedService resortRoomCategoryBedService,
                                  RoomPricingResolver roomPricingResolver,
                                  CurrencyService currencyService) {
        this.availabilityService = availabilityService;
        this.resortService = resortService;
        this.resortRoomService = resortRoomService;
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.resortRoomCategoryBedService = resortRoomCategoryBedService;
        this.roomPricingResolver = roomPricingResolver;
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<?> search(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject AvailabilityRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());

        List<ResortRoomEntity> rooms = availabilityService.search(resortId, request);

        Map<Long, ResortRoomCategoryMetaDto> metaFallbackByCategoryId = new HashMap<>();
        Map<Long, List<ResortRoomCategoryBedDto>> bedsFallbackByCategoryId = new HashMap<>();

        List<AvailableRoomDto> data = rooms.stream()
                .map(room -> buildAvailableRoomDto(resortEntity, currencyEntity, request, room,
                        metaFallbackByCategoryId, bedsFallbackByCategoryId))
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(new AvailabilityResponse(data));
    }

    /**
     * One room's full available-room entry — its room record plus its resolved price, or {@code null} if
     * pricing reports the room as unpriceable for the requested currency (see {@link RoomPricingResolver}),
     * in which case {@link #search} filters it out.
     */
    private AvailableRoomDto buildAvailableRoomDto(ResortEntity resortEntity, CurrencyEntity currencyEntity,
                                                    AvailabilityRequest request, ResortRoomEntity room,
                                                    Map<Long, ResortRoomCategoryMetaDto> metaFallbackByCategoryId,
                                                    Map<Long, List<ResortRoomCategoryBedDto>> bedsFallbackByCategoryId) {
        Long categoryId = room.getResortRoomCategoryEntity().getId();
        ResortRoomCategoryMetaDto metaFallback = metaFallbackByCategoryId
                .computeIfAbsent(categoryId, this::resolveMetaFallback);
        List<ResortRoomCategoryBedDto> bedsFallback = bedsFallbackByCategoryId
                .computeIfAbsent(categoryId, resortRoomCategoryBedService::getAllActive);
        ResortRoomDto roomDto = resortRoomService.buildDto(room, metaFallback, bedsFallback);

        return roomPricingResolver.tryResolveNightly(resortEntity, room, categoryId, currencyEntity.getId(),
                        request.getCheckIn(), request.getCheckOut())
                .map(nightlyResult -> AvailableRoomDto.builder()
                        .room(roomDto)
                        .price(AvailableRoomPriceMapper.toDto(nightlyResult, currencyEntity))
                        .build())
                .orElse(null);
    }

    private ResortRoomCategoryMetaDto resolveMetaFallback(Long resortRoomCategoryId) {
        ResortRoomCategoryMetaEntity categoryMetaEntity =
                resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(resortRoomCategoryId);
        return ResortRoomCategoryMetaMapper.toDto(categoryMetaEntity).build();
    }
}
