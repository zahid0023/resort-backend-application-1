package com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoRequest {

    @NotNull
    private Short estd;

    private String logoUrl;
}
