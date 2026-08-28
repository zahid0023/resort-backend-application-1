package com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo;

import com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.locale.ResortBasicInfoLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortBasicInfoRequest extends ResortBasicInfoRequest {

    @Valid
    @NotNull
    private ResortBasicInfoLocaleRequest locale;
}
