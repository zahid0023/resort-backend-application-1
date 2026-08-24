package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
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

    @PostMapping("/main")
    public ResponseEntity<?> createMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryPriceGroupRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        PriceTypeEntity basePriceTypeEntity = priceTypeService.getEntityByCode("BAS");
        PriceTypeEntity weekdayPriceTypeEntity = priceTypeService.getEntityByCode("WKD");
        PriceTypeEntity weekendPriceTypeEntity = priceTypeService.getEntityByCode("WKE");
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        PriceUnitEntity basePriceUnitEntity = priceUnitService.getEntityById(request.getBasePriceUnitId());
        PriceUnitEntity weekdayPriceUnitEntity = priceUnitService.getEntityById(request.getWeekdayPriceUnitId());
        PriceUnitEntity weekendPriceUnitEntity = priceUnitService.getEntityById(request.getWeekendPriceUnitId());
        List<DayOfWeekEntity> weekdayDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekdayDayOfWeekIds());
        List<DayOfWeekEntity> weekendDayOfWeekEntities = resolveDayOfWeekEntities(request.getWeekendDayOfWeekIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createMain(
                request, resortRoomCategoryEntity, basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                currencyEntity, basePriceUnitEntity, weekdayPriceUnitEntity, weekendPriceUnitEntity,
                weekdayDayOfWeekEntities, weekendDayOfWeekEntities));
    }

    @PostMapping("/holidays")
    public ResponseEntity<?> createHoliday(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryHolidayPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        PriceTypeEntity holidayPriceTypeEntity = priceTypeService.getEntityByCode("HOL");
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createHoliday(
                request, resortRoomCategoryEntity, holidayPriceTypeEntity, priceUnitEntity, currencyEntity));
    }

    @PostMapping("/specials")
    public ResponseEntity<?> createSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategorySpecialPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
        PriceTypeEntity specialPriceTypeEntity = priceTypeService.getEntityByCode("SPECIAL");
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createSpecial(
                request, resortRoomCategoryEntity, specialPriceTypeEntity, priceUnitEntity, currencyEntity));
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
