package com.example.resortbackendapplication1.dayofweek.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface DayOfWeekLocaleService {

    SuccessResponse create(DayOfWeekEntity dayOfWeekEntity,
                           LocaleEntity localeEntity,
                           CreateDayOfWeekLocaleRequest request);

    DayOfWeekLocaleEntity getEntityById(Long dayOfWeekId, Long id);

    SuccessResponse update(DayOfWeekLocaleEntity entity, UpdateDayOfWeekLocaleRequest request);

    SuccessResponse delete(DayOfWeekLocaleEntity entity);
}
