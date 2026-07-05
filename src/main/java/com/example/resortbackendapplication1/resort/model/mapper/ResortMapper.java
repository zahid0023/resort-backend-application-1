package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortMapper {

    public ResortEntity create(CreateResortRequest request) {
        ResortEntity entity = new ResortEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortEntity entity, UpdateResortRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortEntity entity, ResortRequest request) {
        // shared mutable fields applied here as they are added
    }

    public ResortDto toDto(ResortEntity entity) {
        return ResortDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .build();
    }
}
