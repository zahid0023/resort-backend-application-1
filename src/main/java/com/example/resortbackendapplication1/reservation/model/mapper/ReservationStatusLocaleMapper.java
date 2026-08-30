package com.example.resortbackendapplication1.reservation.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.ReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.UpdateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservationStatusLocaleMapper {

    public ReservationStatusLocaleEntity create(ReservationStatusLocaleRequest request) {
        ReservationStatusLocaleEntity entity = new ReservationStatusLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ReservationStatusLocaleEntity entity, UpdateReservationStatusLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ReservationStatusLocaleEntity entity, ReservationStatusLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ReservationStatusLocaleDto toDto(ReservationStatusLocaleEntity entity) {
        return ReservationStatusLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
