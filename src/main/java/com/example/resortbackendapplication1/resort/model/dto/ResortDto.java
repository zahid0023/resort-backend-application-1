package com.example.resortbackendapplication1.resort.model.dto;

import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoDto;
import com.example.resortbackendapplication1.resortcontact.model.dto.ResortContactDto;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortDto {
    private Long id;
    private String code;
    private ResortBasicInfoDto resortBasicInfo;
    private List<ResortContactDto> resortContacts;
}
