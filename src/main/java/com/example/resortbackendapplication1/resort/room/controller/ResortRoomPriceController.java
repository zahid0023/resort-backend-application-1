package com.example.resortbackendapplication1.resort.room.controller;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.service.PriceUnitService;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.ResortRoomDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryPriceService;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomPriceService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.core.service.ResortWeeklyScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/prices")
public class ResortRoomPriceController {

    private final ResortRoomPriceService resortRoomPriceService;
    private final ResortRoomCategoryPriceService resortRoomCategoryPriceService;
    private final ResortRoomService resortRoomService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortWeeklyScheduleService resortWeeklyScheduleService;
    private final PriceUnitService priceUnitService;
    private final CurrencyService currencyService;

    public ResortRoomPriceController(ResortRoomPriceService resortRoomPriceService,
                                     ResortRoomCategoryPriceService resortRoomCategoryPriceService,
                                     ResortRoomService resortRoomService,
                                     ResortRoomCategoryService resortRoomCategoryService,
                                     ResortWeeklyScheduleService resortWeeklyScheduleService,
                                     PriceUnitService priceUnitService,
                                     CurrencyService currencyService) {
        this.resortRoomPriceService = resortRoomPriceService;
        this.resortRoomCategoryPriceService = resortRoomCategoryPriceService;
        this.resortRoomService = resortRoomService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortWeeklyScheduleService = resortWeeklyScheduleService;
        this.priceUnitService = priceUnitService;
        this.currencyService = currencyService;
    }

    @PostMapping("/main")
    public ResponseEntity<?> createMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomMainPriceRequest request) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomPriceService.createMain(
                request, resortRoomEntity, currencyEntity, priceUnitEntity));
    }

    @PostMapping("/specials")
    public ResponseEntity<?> createSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @Valid @RequestBody CreateResortRoomSpecialPriceRequest request) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        boolean categoryHasActiveMain = resortRoomCategoryPriceService.hasActiveMain(
                resortRoomCategoryId, currencyEntity.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resortRoomPriceService.createSpecial(
                request, resortRoomEntity, currencyEntity, priceUnitEntity, categoryHasActiveMain));
    }

    /**
     * Main and Specials are resolved independently — the room's own override is returned for whichever side it
     * has, and the category's bundle for whichever side it doesn't (see {@code mainInherited}/
     * {@code specialsInherited} on the response). The category fallback is resolved here, not in
     * {@code ResortRoomPriceServiceImpl}, since a ServiceImpl must never call another entity's Service.
     */
    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @RequestParam("currency-id") Long currencyId) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays = resortWeeklyScheduleService
                .getEntitiesByDayType(resortRoomEntity.getResortRoomCategoryEntity().getResortEntity(), DayType.WEEKDAY);
        List<ResortWeeklyScheduleDayEntity> weekendScheduleDays = resortWeeklyScheduleService
                .getEntitiesByDayType(resortRoomEntity.getResortRoomCategoryEntity().getResortEntity(), DayType.WEEKEND);
        var categoryFallback = resortRoomCategoryPriceService.getAllGroupedByCurrency(
                resortRoomCategoryId, currencyEntity, weekdayScheduleDays, weekendScheduleDays).getData();
        return ResponseEntity.ok(resortRoomPriceService.getAllGroupedByCurrency(
                resortRoomId, currencyEntity, weekdayScheduleDays, weekendScheduleDays, categoryFallback));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        return ResponseEntity.ok(resortRoomPriceService.getCount(resortRoomId));
    }

    @PutMapping("/main")
    public ResponseEntity<?> updateMain(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @RequestParam("currency-id") Long currencyId,
            @Valid @RequestBody UpdateResortRoomMainPriceRequest request) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        PriceUnitEntity priceUnitEntity = priceUnitService.getEntityById(request.getPriceUnitId());
        return ResponseEntity.ok(resortRoomPriceService.updateMain(
                resortRoomEntity, currencyEntity, request, priceUnitEntity));
    }

    @PutMapping("/specials/{id}")
    public ResponseEntity<?> updateSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRoomSpecialPriceRequest request) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        ResortRoomSpecialPriceEntity entity = resortRoomPriceService.getSpecialEntityById(resortRoomId, id);
        PriceUnitEntity priceUnitEntity = resolvePriceUnit(request);
        return ResponseEntity.ok(resortRoomPriceService.updateSpecial(entity, request, priceUnitEntity));
    }

    @DeleteMapping("/specials/{id}")
    public ResponseEntity<?> deleteSpecial(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @PathVariable Long id) {
        resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        ResortRoomSpecialPriceEntity entity = resortRoomPriceService.getSpecialEntityById(resortRoomId, id);
        return ResponseEntity.ok(resortRoomPriceService.deleteSpecial(entity));
    }

    /**
     * Deletes every override (main + special) for one currency at once, reverting the room back to inheriting
     * that currency's price from its category. Mirrors {@link #getAll} — same path, currency scoped by query
     * param.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteByCurrency(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("resort-room-id") Long resortRoomId,
            @RequestParam("currency-id") Long currencyId) {
        ResortRoomEntity resortRoomEntity = resolveRoom(resortId, resortRoomCategoryId, resortRoomId);
        CurrencyEntity currencyEntity = currencyService.getEntityById(currencyId);
        return ResponseEntity.ok(resortRoomPriceService.deleteByCurrency(resortRoomEntity, currencyEntity));
    }

    private ResortRoomEntity resolveRoom(Long resortId, Long resortRoomCategoryId, Long resortRoomId) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        return resortRoomService.getEntityById(resortRoomCategoryEntity.getId(), resortRoomId);
    }

    private PriceUnitEntity resolvePriceUnit(ResortRoomDateRangePriceRequest request) {
        return priceUnitService.getEntityById(request.getPriceUnitId());
    }
}
