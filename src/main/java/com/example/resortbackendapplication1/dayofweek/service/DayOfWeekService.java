package com.example.resortbackendapplication1.dayofweek.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import com.example.resortbackendapplication1.dayofweek.dto.response.daysofweek.DayOfWeekResponse;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;

public interface DayOfWeekService {

    DayOfWeekEntity getEntityById(Long id);

    DayOfWeekResponse getById(Long id);

    PaginatedResponse<DayOfWeekDto> getAll(DayOfWeekFilterRequest request);
}
