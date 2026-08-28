package com.example.resortbackendapplication1.resort.core.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoEntity;

public interface ResortBasicInfoService {

    ResortBasicInfoEntity getEntityByResortId(Long resortId);

    SuccessResponse update(ResortBasicInfoEntity entity,
                           UpdateResortBasicInfoRequest request);
}
