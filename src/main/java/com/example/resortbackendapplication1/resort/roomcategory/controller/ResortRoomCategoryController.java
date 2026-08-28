package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.service.BedTypeService;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryService;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.service.UnitService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories")
public class ResortRoomCategoryController {

    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortService resortService;
    private final RoomCategoryService roomCategoryService;
    private final LocaleService localeService;
    private final UnitService unitService;
    private final BedTypeService bedTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortRoomCategoryController(ResortRoomCategoryService resortRoomCategoryService,
                                        ResortService resortService,
                                        RoomCategoryService roomCategoryService,
                                        LocaleService localeService,
                                        UnitService unitService,
                                        BedTypeService bedTypeService,
                                        PriceUnitService priceUnitService,
                                        CurrencyService currencyService) {
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortService = resortService;
        this.roomCategoryService = roomCategoryService;
        this.localeService = localeService;
        this.unitService = unitService;
        this.bedTypeService = bedTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortRoomCategoryRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);
        RoomCategoryEntity roomCategoryEntity = roomCategoryService.getEntityById(request.getRoomCategoryId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        UnitEntity roomSizeUnitEntity = request.getMeta().getRoomSizeUnitId() != null
                ? unitService.getEntityById(request.getMeta().getRoomSizeUnitId())
                : null;
        Set<Long> bedTypeIds = request.getBeds().stream()
                .map(CreateResortRoomCategoryBedRequest::getBedTypeId)
                .collect(Collectors.toSet());
        List<BedTypeEntity> bedTypeEntities = bedTypeService.getAll(bedTypeIds);

        List<CurrencyEntity> currencyEntities = request.getPrices().stream()
                .map(CreateResortRoomCategoryMainPriceRequest::getCurrencyId)
                .distinct()
                .map(currencyService::getEntityById)
                .toList();
        List<PriceUnitEntity> priceUnitEntities = request.getPrices().stream()
                .map(CreateResortRoomCategoryMainPriceRequest::getPriceUnitId)
                .distinct()
                .map(priceUnitService::getEntityById)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortRoomCategoryService.create(request, resortEntity, roomCategoryEntity, localeEntity, roomSizeUnitEntity, bedTypeEntities,
                        currencyEntities, priceUnitEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        return ResponseEntity.ok(resortRoomCategoryService.getById(resortId, id));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getActiveCount(@PathVariable("resort-id") Long resortId) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortRoomCategoryService.getActiveRoomCategoryCount(resortId));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortRoomCategoryFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortRoomCategoryService.getAll(resortId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryEntity entity = resortRoomCategoryService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortRoomCategoryService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        ResortRoomCategoryEntity entity = resortRoomCategoryService.getEntityById(resortId, id);
        return ResponseEntity.ok(resortRoomCategoryService.delete(entity));
    }
}
