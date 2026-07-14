package com.example.resortbackendapplication1.resort.dto.request;

import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRequest extends ResortRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    @NotNull
    private CreateResortBasicInfoRequest basicInfo;

    @NotNull
    private List<CreateResortContactRequest> contacts;
}