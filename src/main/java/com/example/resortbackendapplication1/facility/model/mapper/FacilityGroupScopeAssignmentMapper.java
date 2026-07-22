package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentId;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityGroupScopeAssignmentMapper {

    public FacilityGroupScopeAssignmentEntity create(FacilityGroupEntity facilityGroupEntity,
                                                      FacilityScopeEntity facilityScopeEntity) {
        FacilityGroupScopeAssignmentEntity entity = new FacilityGroupScopeAssignmentEntity();
        entity.setId(new FacilityGroupScopeAssignmentId(facilityGroupEntity.getId(), facilityScopeEntity.getId()));
        entity.setFacilityGroupEntity(facilityGroupEntity);
        entity.setFacilityScopeEntity(facilityScopeEntity);
        return entity;
    }

    public FacilityGroupScopeAssignmentDto toDto(FacilityGroupScopeAssignmentEntity entity) {
        FacilityScopeEntity scope = entity.getFacilityScopeEntity();
        List<FacilityScopeLocaleDto> locales = scope.getFacilityScopeLocaleEntities().stream()
                .filter(l -> Boolean.TRUE.equals(l.getIsActive()) && Boolean.FALSE.equals(l.getIsDeleted()))
                .map(FacilityScopeLocaleMapper::toDto)
                .toList();
        return FacilityGroupScopeAssignmentDto.builder()
                .facilityScopeId(scope.getId())
                .code(scope.getCode())
                .sortOrder(scope.getSortOrder())
                .locales(locales)
                .build();
    }
}
