package com.example.resortbackendapplication1.resortbasicinfo.service;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.imagehosting.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.response.ResortBasicInfoResponse;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;

public interface ResortBasicInfoService {

    ResortBasicInfoEntity getEntityByResortId(Long resortId);

    ResortBasicInfoResponse getByResortId(Long resortId);

    SuccessResponse update(ResortBasicInfoEntity entity,
                           UpdateResortBasicInfoRequest request,
                           CountryEntity countryEntity,
                           CityEntity cityEntity);

    SuccessResponse uploadLogo(ResortBasicInfoEntity entity, ImageUploadResponse imageUploadResponse);
}
