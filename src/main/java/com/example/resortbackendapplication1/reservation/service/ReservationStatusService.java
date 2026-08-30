package com.example.resortbackendapplication1.reservation.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.CreateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.ReservationStatusFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.UpdateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.response.reservationstatuses.ReservationStatusResponse;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;

public interface ReservationStatusService {

    SuccessResponse create(CreateReservationStatusRequest request, LocaleEntity localeEntity);

    ReservationStatusEntity getEntityById(Long id);

    ReservationStatusResponse getById(Long id);

    PaginatedResponse<ReservationStatusDto> getAll(ReservationStatusFilterRequest request);

    SuccessResponse update(ReservationStatusEntity entity, UpdateReservationStatusRequest request);

    SuccessResponse delete(ReservationStatusEntity entity);
}
