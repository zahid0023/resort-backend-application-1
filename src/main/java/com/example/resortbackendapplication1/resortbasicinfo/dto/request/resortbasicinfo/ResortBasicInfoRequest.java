package com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoRequest {

    @NotNull
    private Integer sortOrder;

    @NotNull
    private Short estd;

    @NotNull
    private Long countryId;

    @NotNull
    private Long cityId;

    private String logoUrl;
    private Double lat;
    private Double lon;
}
