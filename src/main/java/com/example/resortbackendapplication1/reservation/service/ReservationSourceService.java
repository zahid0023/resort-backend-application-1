package com.example.resortbackendapplication1.reservation.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.CreateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.ReservationSourceFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.UpdateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.response.reservationsources.ReservationSourceResponse;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;

public interface ReservationSourceService {

    SuccessResponse create(CreateReservationSourceRequest request, LocaleEntity localeEntity);

    ReservationSourceEntity getEntityById(Long id);

    ReservationSourceResponse getById(Long id);

    PaginatedResponse<ReservationSourceDto> getAll(ReservationSourceFilterRequest request);

    SuccessResponse update(ReservationSourceEntity entity, UpdateReservationSourceRequest request);

    SuccessResponse delete(ReservationSourceEntity entity);
}
