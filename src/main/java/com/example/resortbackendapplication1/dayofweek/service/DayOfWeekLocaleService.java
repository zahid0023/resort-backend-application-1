package com.example.resortbackendapplication1.dayofweek.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface DayOfWeekLocaleService {
    SuccessResponse create(CreateDayOfWeekLocaleRequest request,
                           DayOfWeekEntity dayOfWeekEntity,
                           LocaleEntity localeEntity);

    DayOfWeekLocaleEntity getEntityById(Long dayOfWeekId, Long id);

    PaginatedResponse<DayOfWeekLocaleDto> getAll(Long dayOfWeekId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(DayOfWeekLocaleEntity entity,
                           UpdateDayOfWeekLocaleRequest request);

    SuccessResponse delete(DayOfWeekLocaleEntity entity);
}
