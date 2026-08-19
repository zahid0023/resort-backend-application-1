package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;

public interface ResortBasicInfoService {

    ResortBasicInfoEntity getEntityByResortId(Long resortId);

    SuccessResponse update(ResortBasicInfoEntity entity,
                           UpdateResortBasicInfoRequest request);
}
