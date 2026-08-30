package com.example.resortbackendapplication1.reservation.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.CreateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.ReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.UpdateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusDto;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ReservationStatusMapper {

    public ReservationStatusEntity create(CreateReservationStatusRequest request) {
        ReservationStatusEntity entity = new ReservationStatusEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ReservationStatusEntity entity, UpdateReservationStatusRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ReservationStatusEntity entity, ReservationStatusRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ReservationStatusDto.ReservationStatusDtoBuilder toDto(ReservationStatusEntity entity) {
        return ReservationStatusDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<ReservationStatusLocaleEntity> activeLocales(ReservationStatusEntity entity) {
        return entity.getReservationStatusLocaleEntities().stream()
                .filter(reservationStatusLocaleEntity -> Boolean.TRUE.equals(reservationStatusLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(reservationStatusLocaleEntity.getIsDeleted()))
                .toList();
    }

    private ReservationStatusLocaleDto singleLocale(ReservationStatusEntity entity) {
        ReservationStatusLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ReservationStatusLocaleMapper.toDto(matched);
    }

    private ReservationStatusLocaleEntity matchLocale(ReservationStatusEntity entity, Long localeId) {
        List<ReservationStatusLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(reservationStatusLocaleEntity -> reservationStatusLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(reservationStatusLocaleEntity -> "en".equals(reservationStatusLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
