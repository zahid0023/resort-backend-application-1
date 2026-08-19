package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.resort.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortMapper {

    public ResortEntity create(CreateResortRequest request) {
        ResortEntity entity = new ResortEntity();
        entity.setCode(request.getCode());
        return entity;
    }

    public void update(ResortEntity entity, UpdateResortRequest request) {
        // Resort has no fields beyond the immutable code (Create-only) — nothing to apply.
    }

    public ResortDto.ResortDtoBuilder toDto(ResortEntity entity) {
        return ResortDto.builder()
                .id(entity.getId())
                .code(entity.getCode());
    }
}
