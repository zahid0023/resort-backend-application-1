package com.example.resortbackendapplication1.reservation.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.CreateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.UpdateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;

public interface ReservationSourceLocaleService {
    SuccessResponse create(CreateReservationSourceLocaleRequest request,
                           ReservationSourceEntity reservationSourceEntity,
                           LocaleEntity localeEntity);

    ReservationSourceLocaleEntity getEntityById(Long reservationSourceId, Long id);

    PaginatedResponse<ReservationSourceLocaleDto> getAll(Long reservationSourceId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long reservationSourceId);

    SuccessResponse update(ReservationSourceLocaleEntity entity, UpdateReservationSourceLocaleRequest request);

    SuccessResponse delete(ReservationSourceLocaleEntity entity);
}
