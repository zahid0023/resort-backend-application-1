package com.example.resortbackendapplication1.resortaccesstype.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.UpdateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;

public interface ResortAccessTypeLocaleService {

    SuccessResponse create(ResortAccessTypeEntity resortAccessTypeEntity,
                           LocaleEntity localeEntity,
                           CreateResortAccessTypeLocaleRequest request);

    ResortAccessTypeLocaleEntity getEntityById(Long resortAccessTypeId, Long id);

    SuccessResponse update(ResortAccessTypeLocaleEntity entity, UpdateResortAccessTypeLocaleRequest request);

    SuccessResponse delete(ResortAccessTypeLocaleEntity entity);
}
