package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortfacilities.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.response.resortfacilities.ResortFacilityResponse;
import com.example.resortbackendapplication1.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import org.springframework.data.domain.Pageable;

public interface ResortFacilityService {

    SuccessResponse createResortFacility(CreateResortFacilityRequest request,
                                         ResortFacilityGroupEntity resortFacilityGroupEntity,
                                         FacilityEntity facilityEntity);

    ResortFacilityEntity getResortFacilityEntity(Long resortFacilityGroupId, Long id);

    ResortFacilityResponse getResortFacility(Long resortFacilityGroupId, Long id);

    PaginatedResponse<ResortFacilityDto> getAllResortFacilities(Long resortFacilityGroupId, Pageable pageable);

    SuccessResponse updateResortFacility(ResortFacilityEntity entity, UpdateResortFacilityRequest request);

    SuccessResponse deleteResortFacility(Long resortFacilityGroupId, Long id);
}
