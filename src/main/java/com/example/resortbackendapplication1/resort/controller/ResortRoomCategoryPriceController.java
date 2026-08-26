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
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
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
            @Valid @RequestBody CreateResortRoomCategoryMainPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, roomCategoryId);
        PriceTypeEntity basePriceTypeEntity = priceTypeService.getEntityByCode("BAS");
        PriceTypeEntity weekdayPriceTypeEntity = priceTypeService.getEntityByCode("WKD");
        PriceTypeEntity weekendPriceTypeEntity = priceTypeService.getEntityByCode("WKE");
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        MainPriceUnits units = resolveMainPriceUnits(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createMain(
                request, resortRoomCategoryEntity, basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                currencyEntity, units.basePriceUnitEntity(), units.weekdayPriceUnitEntity(), units.weekendPriceUnitEntity(),
                units.weekdayDayOfWeekEntities(), units.weekendDayOfWeekEntities()));
    }

    @PostMapping("/holidays")
    public ResponseEntity<?> createHoliday(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryHolidayPriceRequest request) {
        DateRangePriceContext context = resolveDateRangeContext(
                resortId, roomCategoryId, "HOL", request.getPriceUnitId(), request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createHoliday(
                request, context.resortRoomCategoryEntity(), context.priceTypeEntity(), context.priceUnitEntity(), context.currencyEntity()));
    }

    @PostMapping("/specials")
    public ResponseEntity<?> createSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @Valid @RequestBody CreateResortRoomCategorySpecialPriceRequest request) {
        DateRangePriceContext context = resolveDateRangeContext(
                resortId, roomCategoryId, "SPECIAL", request.getPriceUnitId(), request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createSpecial(
                request, context.resortRoomCategoryEntity(), context.priceTypeEntity(), context.priceUnitEntity(), context.currencyEntity()));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @RequestParam("currency-id") Long currencyId) {
        resolveRoomCategory(resortId, roomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getAllGroupedByCurrency(roomCategoryId, currencyEntity));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId) {
        resolveRoomCategory(resortId, roomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getCount(roomCategoryId));
    }

    @PutMapping("/main")
    public ResponseEntity<?> updateMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @RequestParam("currency-id") Long currencyId,
            @Valid @RequestBody UpdateResortRoomCategoryMainPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, roomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        PriceTypeEntity basePriceTypeEntity = priceTypeService.getEntityByCode("BAS");
        PriceTypeEntity weekdayPriceTypeEntity = priceTypeService.getEntityByCode("WKD");
        PriceTypeEntity weekendPriceTypeEntity = priceTypeService.getEntityByCode("WKE");
        MainPriceUnits units = resolveMainPriceUnits(request);
        return ResponseEntity.ok(resortRoomCategoryPriceService.updateMain(
                resortRoomCategoryEntity, currencyEntity, request,
                basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                units.basePriceUnitEntity(), units.weekdayPriceUnitEntity(), units.weekendPriceUnitEntity(),
                units.weekdayDayOfWeekEntities(), units.weekendDayOfWeekEntities()));
    }

    @PutMapping("/holidays/{id}")
    public ResponseEntity<?> updateHoliday(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategoryHolidayPriceRequest request) {
        ResortRoomCategoryPriceEntity entity = resolvePriceEntity(resortId, roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.updateHoliday(entity, request, resolvePriceUnit(request)));
    }

    @PutMapping("/specials/{id}")
    public ResponseEntity<?> updateSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategorySpecialPriceRequest request) {
        ResortRoomCategoryPriceEntity entity = resolvePriceEntity(resortId, roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.updateSpecial(entity, request, resolvePriceUnit(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("room-category-id") Long roomCategoryId,
            @PathVariable Long id) {
        ResortRoomCategoryPriceEntity entity = resolvePriceEntity(resortId, roomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.delete(entity));
    }

    private ResortRoomCategoryEntity resolveRoomCategory(Long resortId, Long roomCategoryId) {
        return resortRoomCategoryService.getEntityById(resortId, roomCategoryId);
    }

    private ResortRoomCategoryPriceEntity resolvePriceEntity(Long resortId, Long roomCategoryId, Long id) {
        resolveRoomCategory(resortId, roomCategoryId);
        return resortRoomCategoryPriceService.getEntityById(roomCategoryId, id);
    }

    private DateRangePriceContext resolveDateRangeContext(Long resortId, Long roomCategoryId, String priceTypeCode,
                                                          Long priceUnitId, Long currencyId) {
        return new DateRangePriceContext(
                resolveRoomCategory(resortId, roomCategoryId),
                priceTypeService.getEntityByCode(priceTypeCode),
                priceUnitService.getEntityById(priceUnitId),
                currencyService.getEntityById(currencyId));
    }

    private PriceUnitEntity resolvePriceUnit(ResortRoomCategoryPriceRequest request) {
        return priceUnitService.getEntityById(request.getPriceUnitId());
    }

    private MainPriceUnits resolveMainPriceUnits(ResortRoomCategoryMainPriceRequest request) {
        return new MainPriceUnits(
                resolvePriceUnit(request.getBasePriceRequest()),
                resolvePriceUnit(request.getWeekdayPrice()),
                resolvePriceUnit(request.getWeekendPrice()),
                resolveDayOfWeekEntities(request.getWeekdayPrice().getDayOfWeekIds()),
                resolveDayOfWeekEntities(request.getWeekendPrice().getDayOfWeekIds()));
    }

    private List<DayOfWeekEntity> resolveDayOfWeekEntities(List<Long> dayOfWeekIds) {
        if (dayOfWeekIds == null || dayOfWeekIds.isEmpty()) {
            return List.of();
        }
        return dayOfWeekIds.stream()
                .map(dayOfWeekService::getEntityById)
                .toList();
    }

    private record DateRangePriceContext(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                         PriceTypeEntity priceTypeEntity,
                                         PriceUnitEntity priceUnitEntity,
                                         CurrencyEntity currencyEntity) {
    }

    private record MainPriceUnits(PriceUnitEntity basePriceUnitEntity,
                                  PriceUnitEntity weekdayPriceUnitEntity,
                                  PriceUnitEntity weekendPriceUnitEntity,
                                  List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                  List<DayOfWeekEntity> weekendDayOfWeekEntities) {
    }
}
