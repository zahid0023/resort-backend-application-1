package com.example.resortbackendapplication1.unittype.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.CreateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale.UpdateUnitTypeLocaleRequest;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeEntity;
import com.example.resortbackendapplication1.unittype.model.entity.UnitTypeLocaleEntity;

public interface UnitTypeLocaleService {

    SuccessResponse create(UnitTypeEntity unitTypeEntity,
                           LocaleEntity localeEntity,
                           CreateUnitTypeLocaleRequest request);

    UnitTypeLocaleEntity getEntityById(Long unitTypeId, Long id);

    SuccessResponse update(UnitTypeLocaleEntity entity,
                           UpdateUnitTypeLocaleRequest request);

    SuccessResponse delete(UnitTypeLocaleEntity entity);
}
