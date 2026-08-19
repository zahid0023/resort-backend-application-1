package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityOperatingHoursDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityOperatingHoursEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class ResortFacilityOperatingHoursMapper {

    /** Builds a bare row (no parent attachment) — every write goes through the whole-week schedule shape. */
    public ResortFacilityOperatingHoursEntity create(Boolean isClosed, Boolean isTwentyFourHours,
                                                       LocalTime opensAt, LocalTime closesAt) {
        ResortFacilityOperatingHoursEntity entity = new ResortFacilityOperatingHoursEntity();
        entity.setIsClosed(isClosed);
        entity.setIsTwentyFourHours(isTwentyFourHours);
        entity.setOpensAt(opensAt);
        entity.setClosesAt(closesAt);
        return entity;
    }

    public ResortFacilityOperatingHoursDto.ResortFacilityOperatingHoursDtoBuilder toDto(ResortFacilityOperatingHoursEntity entity) {
        return ResortFacilityOperatingHoursDto.builder()
                .id(entity.getId())
                .opensAt(entity.getOpensAt())
                .closesAt(entity.getClosesAt())
                .isClosed(entity.getIsClosed())
                .isTwentyFourHours(entity.getIsTwentyFourHours());
    }
}
