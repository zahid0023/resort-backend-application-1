package com.example.resortbackendapplication1.resort.roomcategory.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.ResortRoomCategoryDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategorySpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryPriceService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.core.service.ResortWeeklyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices")
public class ResortRoomCategoryPriceController {

    private final ResortRoomCategoryPriceService resortRoomCategoryPriceService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortWeeklyScheduleService resortWeeklyScheduleService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortRoomCategoryPriceController(ResortRoomCategoryPriceService resortRoomCategoryPriceService,
                                             ResortRoomCategoryService resortRoomCategoryService,
                                             ResortWeeklyScheduleService resortWeeklyScheduleService,
                                             PriceUnitService priceUnitService,
                                             CurrencyService currencyService) {
        this.resortRoomCategoryPriceService = resortRoomCategoryPriceService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortWeeklyScheduleService = resortWeeklyScheduleService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping("/main")
    public ResponseEntity<?> createMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategoryMainPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, resortRoomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createMain(
                request, resortRoomCategoryEntity, currencyEntity, priceUnitEntity));
    }

    @PostMapping("/specials")
    public ResponseEntity<?> createSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @Valid @RequestBody CreateResortRoomCategorySpecialPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, resortRoomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomCategoryPriceService.createSpecial(
                request, resortRoomCategoryEntity, currencyEntity, priceUnitEntity));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @RequestParam("currency-id") Long currencyId) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, resortRoomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays = resortWeeklyScheduleService
                .getEntitiesByDayType(resortRoomCategoryEntity.getResortEntity(), DayType.WEEKDAY);
        List<ResortWeeklyScheduleDayEntity> weekendScheduleDays = resortWeeklyScheduleService
                .getEntitiesByDayType(resortRoomCategoryEntity.getResortEntity(), DayType.WEEKEND);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getAllGroupedByCurrency(
                resortRoomCategoryId, currencyEntity, weekdayScheduleDays, weekendScheduleDays));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId) {
        resolveRoomCategory(resortId, resortRoomCategoryId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.getCount(resortRoomCategoryId));
    }

    @PutMapping("/main")
    public ResponseEntity<?> updateMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @RequestParam("currency-id") Long currencyId,
            @Valid @RequestBody UpdateResortRoomCategoryMainPriceRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, resortRoomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.ok(resortRoomCategoryPriceService.updateMain(
                resortRoomCategoryEntity, currencyEntity, request, priceUnitEntity));
    }

    @PutMapping("/specials/{id}")
    public ResponseEntity<?> updateSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomCategorySpecialPriceRequest request) {
        resolveRoomCategory(resortId, resortRoomCategoryId);
        ResortRoomCategorySpecialPriceEntity entity = resortRoomCategoryPriceService.getSpecialEntityById(resortRoomCategoryId, id);
        PriceUnitEntity priceUnitEntity = resolvePriceUnit(request);
        return ResponseEntity.ok(resortRoomCategoryPriceService.updateSpecial(entity, request, priceUnitEntity));
    }

    @DeleteMapping("/specials/{id}")
    public ResponseEntity<?> deleteSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable Long id) {
        resolveRoomCategory(resortId, resortRoomCategoryId);
        ResortRoomCategorySpecialPriceEntity entity = resortRoomCategoryPriceService.getSpecialEntityById(resortRoomCategoryId, id);
        return ResponseEntity.ok(resortRoomCategoryPriceService.deleteSpecial(entity));
    }

    /**
     * Deletes every price (main + special) for one currency at once — the only way to remove a currency's main
     * price, since special rows require an active main price to exist. Mirrors {@link #getAll} — same path,
     * currency scoped by query param.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteByCurrency(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @RequestParam("currency-id") Long currencyId) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resolveRoomCategory(resortId, resortRoomCategoryId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        return ResponseEntity.ok(resortRoomCategoryPriceService.deleteByCurrency(resortRoomCategoryEntity, currencyEntity));
    }

    private ResortRoomCategoryEntity resolveRoomCategory(Long resortId, Long resortRoomCategoryId) {
        return resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
    }

    private PriceUnitEntity resolvePriceUnit(ResortRoomCategoryDateRangePriceRequest request) {
        return priceUnitService.getEntityById(request.getPriceUnitId());
    }
}
