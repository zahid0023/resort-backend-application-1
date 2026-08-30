package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.commons.utils.CollectionUtils;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomStatusRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryBedService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.service.RoomStatusService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms")
public class ResortRoomController {

    private final ResortRoomService resortRoomService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;
    private final ResortRoomCategoryBedService resortRoomCategoryBedService;
    private final RoomStatusService roomStatusService;
    private final LocaleService localeService;
    private final UnitService unitService;
    private final BedTypeService bedTypeService;
    private final CurrencyService currencyService;
    private final PriceUnitService priceUnitService;

    public ResortRoomController(ResortRoomService resortRoomService,
                                ResortRoomCategoryService resortRoomCategoryService,
                                ResortRoomCategoryMetaService resortRoomCategoryMetaService,
                                ResortRoomCategoryBedService resortRoomCategoryBedService,
                                RoomStatusService roomStatusService,
                                LocaleService localeService,
                                UnitService unitService,
                                BedTypeService bedTypeService,
                                CurrencyService currencyService,
                                PriceUnitService priceUnitService) {
        this.resortRoomService = resortRoomService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
        this.resortRoomCategoryBedService = resortRoomCategoryBedService;
        this.roomStatusService = roomStatusService;
        this.localeService = localeService;
        this.unitService = unitService;
        this.bedTypeService = bedTypeService;
        this.currencyService = currencyService;
        this.priceUnitService = priceUnitService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveResortRoomCategory(resortId, resortRoomCategoryId);
        RoomStatusEntity roomStatusEntity = roomStatusService.getEntityById(request.getRoomStatusId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        UnitEntity roomSizeUnitEntity = request.getMeta() != null && request.getMeta().getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getMeta().getRoomSizeUnitId())
                : null;
        List<CreateResortRoomBedRequest> bedRequests = request.getBeds() != null ? request.getBeds() : List.of();
        Set<Long> bedTypeIds = CollectionUtils.extractIds(bedRequests, CreateResortRoomBedRequest::getBedTypeId);
        List<BedTypeEntity> bedTypeEntities = bedTypeService.getAll(bedTypeIds);

        List<CreateResortRoomMainPriceRequest> priceRequests = request.getPrices() != null ? request.getPrices() : List.of();
        List<CurrencyEntity> currencyEntities = priceRequests.stream()
                .map(CreateResortRoomMainPriceRequest::getCurrencyId)
                .distinct()
                .map(currencyService::getEntityById)
                .toList();
        List<PriceUnitEntity> priceUnitEntities = priceRequests.stream()
                .map(CreateResortRoomMainPriceRequest::getPriceUnitId)
                .distinct()
                .map(priceUnitService::getEntityById)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomService.create(request, resortRoomCategoryEntity, roomStatusEntity, localeEntity,
                        roomSizeUnitEntity, bedTypeEntities, currencyEntities, priceUnitEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resolveResortRoomCategory(resortId, resortRoomCategoryId);
        ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback = resolveResortRoomCategoryMetaFallback(resortRoomCategoryId);
        List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback = resolveResortRoomCategoryBedsFallback(resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomService.getById(resortRoomCategoryId, id, resortRoomCategoryMetaFallback, resortRoomCategoryBedsFallback));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @ParameterObject ResortRoomFilterRequest request) {
        resolveResortRoomCategory(resortId, resortRoomCategoryId);
        ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback = resolveResortRoomCategoryMetaFallback(resortRoomCategoryId);
        List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback = resolveResortRoomCategoryBedsFallback(resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomService.getAll(resortRoomCategoryId, request, resortRoomCategoryMetaFallback, resortRoomCategoryBedsFallback));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomRequest request) {
        ResortRoomEntity entity = resolveResortRoom(resortId, resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomService.update(entity, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomStatusRequest request) {
        ResortRoomEntity entity = resolveResortRoom(resortId, resortRoomCategoryId, id);
        RoomStatusEntity roomStatusEntity = roomStatusService.getEntityById(request.getRoomStatusId());
        return ResponseEntity.ok(resortRoomService.updateStatus(entity, roomStatusEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        ResortRoomEntity entity = resolveResortRoom(resortId, resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomService.delete(entity));
    }

    private ResortRoomCategoryEntity resolveResortRoomCategory(Long resortId, Long resortRoomCategoryId) {
        return resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
    }

    private ResortRoomCategoryMetaDto resolveResortRoomCategoryMetaFallback(Long resortRoomCategoryId) {
        ResortRoomCategoryMetaEntity categoryMetaEntity =
                resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(resortRoomCategoryId);
        return ResortRoomCategoryMetaMapper.toDto(categoryMetaEntity).build();
    }

    private List<ResortRoomCategoryBedDto> resolveResortRoomCategoryBedsFallback(Long resortRoomCategoryId) {
        return resortRoomCategoryBedService.getAllActive(resortRoomCategoryId);
    }

    private ResortRoomEntity resolveResortRoom(Long resortId, Long resortRoomCategoryId, Long id) {
        resolveResortRoomCategory(resortId, resortRoomCategoryId);
        return resortRoomService.getEntityById(resortRoomCategoryId, id);
    }
}
