package com.example.resortbackendapplication1.bookingsource.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.BookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.UpdateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceLocaleDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookingSourceLocaleMapper {

    public BookingSourceLocaleEntity create(BookingSourceLocaleRequest request) {
        BookingSourceLocaleEntity entity = new BookingSourceLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(BookingSourceLocaleEntity entity, UpdateBookingSourceLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(BookingSourceLocaleEntity entity, BookingSourceLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public BookingSourceLocaleDto toDto(BookingSourceLocaleEntity entity) {
        return BookingSourceLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
