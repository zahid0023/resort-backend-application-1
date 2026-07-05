package com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortBasicInfoLocaleRequest extends ResortBasicInfoLocaleRequest {

    @NotNull
    private Long localeId;
}
