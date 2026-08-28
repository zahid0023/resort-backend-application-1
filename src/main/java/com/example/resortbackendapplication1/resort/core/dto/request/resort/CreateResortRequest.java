package com.example.resortbackendapplication1.resort.core.dto.request.resort;

import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.CreateResortAddressRequest;
import com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resort.core.dto.request.resortweeklyschedule.ResortWeeklyScheduleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRequest extends ResortRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private CreateResortBasicInfoRequest basicInfo;

    @Valid
    @NotNull
    private CreateResortAddressRequest address;

    @Valid
    @NotNull
    private ResortWeeklyScheduleRequest weeklySchedule;

}
