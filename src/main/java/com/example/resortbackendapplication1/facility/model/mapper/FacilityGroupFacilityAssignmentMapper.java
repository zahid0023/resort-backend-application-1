package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupFacilityAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupFacilityAssignmentEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FacilityGroupFacilityAssignmentMapper {

    public FacilityGroupFacilityAssignmentEntity create() {
        return new FacilityGroupFacilityAssignmentEntity();
    }

    public FacilityGroupFacilityAssignmentDto.FacilityGroupFacilityAssignmentDtoBuilder toDto(FacilityGroupFacilityAssignmentEntity entity) {
        return FacilityGroupFacilityAssignmentDto.builder()
                .id(entity.getId());
    }
}
