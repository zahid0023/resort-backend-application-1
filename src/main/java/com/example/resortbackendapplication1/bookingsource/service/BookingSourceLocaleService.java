package com.example.resortbackendapplication1.bookingsource.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.CreateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.UpdateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceLocaleDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;

public interface BookingSourceLocaleService {
    SuccessResponse create(CreateBookingSourceLocaleRequest request,
                           BookingSourceEntity bookingSourceEntity,
                           LocaleEntity localeEntity);

    BookingSourceLocaleEntity getEntityById(Long bookingSourceId, Long id);

    PaginatedResponse<BookingSourceLocaleDto> getAll(Long bookingSourceId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long bookingSourceId);

    SuccessResponse update(BookingSourceLocaleEntity entity, UpdateBookingSourceLocaleRequest request);

    SuccessResponse delete(BookingSourceLocaleEntity entity);
}
