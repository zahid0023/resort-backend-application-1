package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityScopeAssignmentMapper {

    public FacilityScopeAssignmentEntity create() {
        return new FacilityScopeAssignmentEntity();
    }

    public FacilityScopeAssignmentDto.FacilityScopeAssignmentDtoBuilder toDto(FacilityScopeAssignmentEntity entity) {
        return FacilityScopeAssignmentDto.builder()
                .id(entity.getId());
    }
}
