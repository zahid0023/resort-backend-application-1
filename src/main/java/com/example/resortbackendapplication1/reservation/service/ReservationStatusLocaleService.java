package com.example.resortbackendapplication1.reservation.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.CreateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.UpdateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;

public interface ReservationStatusLocaleService {
    SuccessResponse create(CreateReservationStatusLocaleRequest request,
                           ReservationStatusEntity reservationStatusEntity,
                           LocaleEntity localeEntity);

    ReservationStatusLocaleEntity getEntityById(Long reservationStatusId, Long id);

    PaginatedResponse<ReservationStatusLocaleDto> getAll(Long reservationStatusId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long reservationStatusId);

    SuccessResponse update(ReservationStatusLocaleEntity entity, UpdateReservationStatusLocaleRequest request);

    SuccessResponse delete(ReservationStatusLocaleEntity entity);
}
