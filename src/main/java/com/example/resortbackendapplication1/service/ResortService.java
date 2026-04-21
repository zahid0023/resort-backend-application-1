package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.dto.response.resorts.ResortResponse;
import com.example.resortbackendapplication1.model.dto.ResortDto;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResortService {

    SuccessResponse createResort(
            CreateResortRequest request,
            UserEntity userEntity,
            ResortAccessTypeEntity resortAccessTypeEntity,
            CountryEntity countryEntity,
            CityEntity cityEntity,
            CreateResortImageStorageConfigRequest createResortImageStorageConfigRequest,
            List<ImageRequest> imageRequests);

    ResortEntity getResortById(Long id);

    ResortResponse getResort(Long id);

    PaginatedResponse<ResortDto> getAllResorts(UserEntity userEntity, Pageable pageable);

    ResortResponse updateResort(Long id, UpdateResortRequest request);

    SuccessResponse deleteResort(Long id);
}
