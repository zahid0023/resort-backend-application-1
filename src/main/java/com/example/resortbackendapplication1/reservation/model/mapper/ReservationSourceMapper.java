package com.example.resortbackendapplication1.reservation.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.CreateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.ReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.UpdateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceDto;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ReservationSourceMapper {

    public ReservationSourceEntity create(CreateReservationSourceRequest request) {
        ReservationSourceEntity entity = new ReservationSourceEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ReservationSourceEntity entity, UpdateReservationSourceRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ReservationSourceEntity entity, ReservationSourceRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ReservationSourceDto.ReservationSourceDtoBuilder toDto(ReservationSourceEntity entity) {
        return ReservationSourceDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<ReservationSourceLocaleEntity> activeLocales(ReservationSourceEntity entity) {
        return entity.getReservationSourceLocaleEntities().stream()
                .filter(reservationSourceLocaleEntity -> Boolean.TRUE.equals(reservationSourceLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(reservationSourceLocaleEntity.getIsDeleted()))
                .toList();
    }

    private ReservationSourceLocaleDto singleLocale(ReservationSourceEntity entity) {
        ReservationSourceLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ReservationSourceLocaleMapper.toDto(matched);
    }

    private ReservationSourceLocaleEntity matchLocale(ReservationSourceEntity entity, Long localeId) {
        List<ReservationSourceLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(reservationSourceLocaleEntity -> reservationSourceLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(reservationSourceLocaleEntity -> "en".equals(reservationSourceLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
