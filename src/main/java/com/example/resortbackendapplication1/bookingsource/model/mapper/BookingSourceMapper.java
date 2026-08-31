package com.example.resortbackendapplication1.bookingsource.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.CreateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.BookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.UpdateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceDto;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceLocaleDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BookingSourceMapper {

    public BookingSourceEntity create(CreateBookingSourceRequest request) {
        BookingSourceEntity entity = new BookingSourceEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(BookingSourceEntity entity, UpdateBookingSourceRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(BookingSourceEntity entity, BookingSourceRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public BookingSourceDto.BookingSourceDtoBuilder toDto(BookingSourceEntity entity) {
        return BookingSourceDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<BookingSourceLocaleEntity> activeLocales(BookingSourceEntity entity) {
        return entity.getBookingSourceLocaleEntities().stream()
                .filter(bookingSourceLocaleEntity -> Boolean.TRUE.equals(bookingSourceLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(bookingSourceLocaleEntity.getIsDeleted()))
                .toList();
    }

    private BookingSourceLocaleDto singleLocale(BookingSourceEntity entity) {
        BookingSourceLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : BookingSourceLocaleMapper.toDto(matched);
    }

    private BookingSourceLocaleEntity matchLocale(BookingSourceEntity entity, Long localeId) {
        List<BookingSourceLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(bookingSourceLocaleEntity -> bookingSourceLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(bookingSourceLocaleEntity -> "en".equals(bookingSourceLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
