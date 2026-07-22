package com.example.resortbackendapplication1.unit.service;

import com.example.resortbackendapplication1.unit.dto.request.unit.unitlocale.CreateUnitLocaleRequest;
import com.example.resortbackendapplication1.unit.dto.request.unit.unitlocale.UpdateUnitLocaleRequest;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface UnitLocaleService {

    SuccessResponse create(UnitEntity unitEntity,
                           LocaleEntity localeEntity,
                           CreateUnitLocaleRequest request);

    UnitLocaleEntity getEntityById(Long unitId, Long id);

    SuccessResponse update(UnitLocaleEntity entity, UpdateUnitLocaleRequest request);

    SuccessResponse delete(UnitLocaleEntity entity);
}
