package com.example.resortbackendapplication1.bedtype.service;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface BedTypeLocaleService {

    SuccessResponse create(BedTypeEntity bedTypeEntity,
                           LocaleEntity localeEntity,
                           CreateBedTypeLocaleRequest request);

    BedTypeLocaleEntity getEntityById(Long bedTypeId, Long id);

    SuccessResponse update(BedTypeLocaleEntity entity,
                           UpdateBedTypeLocaleRequest request);

    SuccessResponse delete(BedTypeLocaleEntity entity);
}
