package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDayDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceDayEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryPriceDayMapper {

    public ResortRoomCategoryPriceDayEntity create() {
        return new ResortRoomCategoryPriceDayEntity();
    }

    public ResortRoomCategoryPriceDayDto.ResortRoomCategoryPriceDayDtoBuilder toDto(ResortRoomCategoryPriceDayEntity entity) {
        return ResortRoomCategoryPriceDayDto.builder()
                .id(entity.getId());
    }
}
