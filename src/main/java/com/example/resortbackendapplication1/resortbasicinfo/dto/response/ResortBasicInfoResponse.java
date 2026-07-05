package com.example.resortbackendapplication1.resortbasicinfo.dto.response;

import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoResponse {
    private final ResortBasicInfoDto resortBasicInfo;

    public ResortBasicInfoResponse(ResortBasicInfoDto resortBasicInfo) {
        this.resortBasicInfo = resortBasicInfo;
    }
}
