package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryPriceService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices")
public class ResortRoomCategoryPriceController {

    private final ResortRoomCategoryPriceService resortRoomCategoryPriceService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final PriceTypeService priceTypeService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;
    private final DayOfWeekService dayOfWeekService;

    public ResortRoomCategoryPriceController(ResortRoomCategoryPriceService resortRoomCategoryPriceService,
                                             ResortRoomCategoryService resortRoomCategoryService,
                                             PriceTypeService priceTypeService,
                                             PriceUnitService priceUnitService,
                                             CurrencyService currencyService,
                                             DayOfWeekService dayOfWeekService) {
        this.resortRoomCategoryPriceService = resortRoomCategoryPriceService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.priceTypeService = priceTypeService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
        this.dayOfWeekService = dayOfWeekService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        PriceTypeEntity priceTypeEntity = priceTypeService.getEntityById(request.getPriceTypeId());
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        List<DayOfWeekEntity> dayOfWeekEntities = resolveDayOfWeekEntities(request.getDayOfWeekIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.create(
                request, resortRoomCategoryEntity, priceTypeEntity, priceUnitEntity, currencyEntity, dayOfWeekEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getById(roomCategoryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @RequestParam("currency-id") Long currencyId) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getAllGroupedByCurrency(roomCategoryId, currencyEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryPriceRequest request) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        ResortRoomCategoryPriceEntity entity = resortRoomCategoryPriceService.getEntityById(roomCategoryId, id);
        List<DayOfWeekEntity> dayOfWeekEntities = resolveDayOfWeekEntities(request.getDayOfWeekIds());
        return ResponseEntity.ok(resortRoomCategoryPriceService.update(entity, request, dayOfWeekEntities));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        ResortRoomCategoryPriceEntity entity = resortRoomCategoryPriceService.getEntityById(roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.delete(entity));
    }

    private List<DayOfWeekEntity> resolveDayOfWeekEntities(List<Long> dayOfWeekIds) {
        if (dayOfWeekIds == null || dayOfWeekIds.isEmpty()) {
            return List.of();
        }
        return dayOfWeekIds.stream()
                .map(dayOfWeekService::getEntityById)
                .toList();
    }
}
