package com.example.resortbackendapplication1.dayofweek.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.UpdateDayOfWeekRequest;
import com.example.resortbackendapplication1.dayofweek.dto.response.daysofweek.DayOfWeekResponse;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;

import java.util.List;
import java.util.Set;

public interface DayOfWeekService {

    DayOfWeekEntity getEntityById(Long id);

    DayOfWeekResponse getById(Long id);

    PaginatedResponse<DayOfWeekDto> getAll(DayOfWeekFilterRequest request);

    SuccessResponse update(DayOfWeekEntity entity, UpdateDayOfWeekRequest request);

    List<DayOfWeekEntity> getAll(Set<Long> ids);
}
