package com.example.resortbackendapplication1.bookingsource.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.CreateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.BookingSourceFilterRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.UpdateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.response.bookingsources.BookingSourceResponse;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;

public interface BookingSourceService {

    SuccessResponse create(CreateBookingSourceRequest request, LocaleEntity localeEntity);

    BookingSourceEntity getEntityById(Long id);

    BookingSourceResponse getById(Long id);

    PaginatedResponse<BookingSourceDto> getAll(BookingSourceFilterRequest request);

    SuccessResponse update(BookingSourceEntity entity, UpdateBookingSourceRequest request);

    SuccessResponse delete(BookingSourceEntity entity);
}
