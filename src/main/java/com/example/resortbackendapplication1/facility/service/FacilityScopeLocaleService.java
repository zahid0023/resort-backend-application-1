package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityScopeLocaleService {

    SuccessResponse create(FacilityScopeEntity facilityScopeEntity,
                           LocaleEntity localeEntity,
                           CreateFacilityScopeLocaleRequest request);

    FacilityScopeLocaleEntity getEntityById(Long facilityScopeId, Long id);

    SuccessResponse update(FacilityScopeLocaleEntity entity, UpdateFacilityScopeLocaleRequest request);

    SuccessResponse delete(FacilityScopeLocaleEntity entity);
}
