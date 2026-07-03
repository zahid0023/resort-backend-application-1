package com.example.resortbackendapplication1.uiblocksection.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.UpdateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;

public interface UiBlockSectionLocaleService {

    SuccessResponse create(UiBlockSectionEntity uiBlockSectionEntity,
                           LocaleEntity localeEntity,
                           CreateUiBlockSectionLocaleRequest request);

    UiBlockSectionLocaleEntity getEntityById(Long uiBlockSectionId, Long id);

    SuccessResponse update(UiBlockSectionLocaleEntity entity,
                           UpdateUiBlockSectionLocaleRequest request);

    SuccessResponse delete(UiBlockSectionLocaleEntity entity);
}
