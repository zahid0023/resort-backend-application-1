package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityGroupScopeAssignmentMapper {

    public FacilityGroupScopeAssignmentEntity create() {
        return new FacilityGroupScopeAssignmentEntity();
    }

    public FacilityGroupScopeAssignmentDto.FacilityGroupScopeAssignmentDtoBuilder toDto(FacilityGroupScopeAssignmentEntity entity) {
        return FacilityGroupScopeAssignmentDto.builder()
                .id(entity.getId());
    }
}
