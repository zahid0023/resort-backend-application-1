package com.example.resortbackendapplication1.resort.controller;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.address.service.CityService;
import com.example.resortbackendapplication1.address.service.CountryService;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.locale.service.LocaleService;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.service.PriceTypeService;
import com.example.resortbackendapplication1.resort.dto.request.resort.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resorts")
public class ResortController {

    private static final String OWNER_ROLE_CODE = "OWNER";
    private static final String ALL_PERMISSIONS_CODE = "ALL_PERMISSIONS";

    private final ResortService resortService;
    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;
    private final LocaleService localeService;
    private final ResortRoleTypeService resortRoleTypeService;
    private final ResortPermissionTypeService resortPermissionTypeService;
    private final PriceTypeService priceTypeService;
    private final DayOfWeekService dayOfWeekService;

    public ResortController(ResortService resortService,
                            UserService userService,
                            CountryService countryService,
                            CityService cityService,
                            LocaleService localeService,
                            ResortRoleTypeService resortRoleTypeService,
                            ResortPermissionTypeService resortPermissionTypeService,
                            PriceTypeService priceTypeService,
                            DayOfWeekService dayOfWeekService) {
        this.resortService = resortService;
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.localeService = localeService;
        this.resortRoleTypeService = resortRoleTypeService;
        this.resortPermissionTypeService = resortPermissionTypeService;
        this.priceTypeService = priceTypeService;
        this.dayOfWeekService = dayOfWeekService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateResortRequest request) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        ResortRoleTypeEntity resortRoleTypeEntity = resortRoleTypeService.getEntityByCode(OWNER_ROLE_CODE);
        ResortPermissionTypeEntity resortPermissionTypeEntity = resortPermissionTypeService.getEntityByCode(ALL_PERMISSIONS_CODE);
        // countryEntity/cityEntity resolve request.getAddress()'s country_id/city_id, since ResortBasicInfo
        // no longer has its own country/city fields.
        CountryEntity countryEntity = countryService.getEntityById(request.getAddress().getCountryId());
        CityEntity cityEntity = cityService.getEntityById(request.getAddress().getCityId());
        LocaleEntity localeEntity = localeService.getEntityByCode("en");
        PriceTypeEntity weekdayPriceTypeEntity = priceTypeService.getEntityByCode("WKD");
        PriceTypeEntity weekendPriceTypeEntity = priceTypeService.getEntityByCode("WKE");
        List<DayOfWeekEntity> weekdayDayOfWeekEntities =
                resolveDayOfWeekEntities(request.getWeeklySchedule().getWeekdayDayOfWeekIds());
        List<DayOfWeekEntity> weekendDayOfWeekEntities =
                resolveDayOfWeekEntities(request.getWeeklySchedule().getWeekendDayOfWeekIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resortService.create(request, userEntity, resortRoleTypeEntity, resortPermissionTypeEntity,
                        countryEntity, cityEntity, localeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                        weekdayDayOfWeekEntities, weekendDayOfWeekEntities));
    }

    private List<DayOfWeekEntity> resolveDayOfWeekEntities(List<Long> dayOfWeekIds) {
        return dayOfWeekIds.stream()
                .map(dayOfWeekService::getEntityById)
                .toList();
    }

    @GetMapping("/my-resorts")
    public ResponseEntity<?> getMyResorts(@Valid @ParameterObject PaginatedRequest request) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        return ResponseEntity.ok(resortService.getMyResorts(userEntity.getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resortService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject ResortFilterRequest request) {
        return ResponseEntity.ok(resortService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResortRequest request) {
        ResortEntity entity = resortService.getEntityById(id);
        return ResponseEntity.ok(resortService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ResortEntity entity = resortService.getEntityById(id);
        return ResponseEntity.ok(resortService.delete(entity));
    }
}
