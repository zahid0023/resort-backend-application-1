package com.example.resortapplication1.service;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortapplication1.dto.response.facilities.FacilityResponse;
import com.example.resortapplication1.model.dto.FacilityDto;
import com.example.resortapplication1.model.entity.FacilityEntity;
import org.springframework.data.domain.Pageable;

public interface FacilityService {

    SuccessResponse createFacility(CreateFacilityRequest request);

    FacilityEntity getFacilityEntity(Long id);

    FacilityResponse getFacility(Long id);

    PaginatedResponse<FacilityDto> getAllFacilities(Pageable pageable);

    SuccessResponse updateFacility(Long id, UpdateFacilityRequest request);

    SuccessResponse deleteFacility(Long id);
}
