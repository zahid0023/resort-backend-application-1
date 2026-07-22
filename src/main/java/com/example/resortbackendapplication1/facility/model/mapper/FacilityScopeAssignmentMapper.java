package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentId;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityScopeAssignmentMapper {

    public FacilityScopeAssignmentEntity create(FacilityEntity facilityEntity,
                                                FacilityScopeEntity facilityScopeEntity) {
        FacilityScopeAssignmentEntity entity = new FacilityScopeAssignmentEntity();
        entity.setId(new FacilityScopeAssignmentId(facilityEntity.getId(), facilityScopeEntity.getId()));
        entity.setFacilityEntity(facilityEntity);
        entity.setFacilityScopeEntity(facilityScopeEntity);
        return entity;
    }

    public FacilityScopeAssignmentDto toDto(FacilityScopeAssignmentEntity entity) {
        FacilityScopeEntity scope = entity.getFacilityScopeEntity();
        List<FacilityScopeLocaleDto> locales = scope.getFacilityScopeLocaleEntities().stream()
                .filter(l -> Boolean.TRUE.equals(l.getIsActive()) && Boolean.FALSE.equals(l.getIsDeleted()))
                .map(FacilityScopeLocaleMapper::toDto)
                .toList();
        return FacilityScopeAssignmentDto.builder()
                .facilityScopeId(scope.getId())
                .code(scope.getCode())
                .sortOrder(scope.getSortOrder())
                .locales(locales)
                .build();
    }
}
