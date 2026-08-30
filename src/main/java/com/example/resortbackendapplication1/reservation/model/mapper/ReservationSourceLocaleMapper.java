package com.example.resortbackendapplication1.reservation.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.ReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.UpdateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservationSourceLocaleMapper {

    public ReservationSourceLocaleEntity create(ReservationSourceLocaleRequest request) {
        ReservationSourceLocaleEntity entity = new ReservationSourceLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ReservationSourceLocaleEntity entity, UpdateReservationSourceLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ReservationSourceLocaleEntity entity, ReservationSourceLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ReservationSourceLocaleDto toDto(ReservationSourceLocaleEntity entity) {
        return ReservationSourceLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
