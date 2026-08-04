package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeAssignmentDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceTypeScopeAssignmentMapper {

    public PriceTypeScopeAssignmentEntity create() {
        return new PriceTypeScopeAssignmentEntity();
    }

    public PriceTypeScopeAssignmentDto.PriceTypeScopeAssignmentDtoBuilder toDto(PriceTypeScopeAssignmentEntity entity) {
        return PriceTypeScopeAssignmentDto.builder()
                .id(entity.getId());
    }
}
