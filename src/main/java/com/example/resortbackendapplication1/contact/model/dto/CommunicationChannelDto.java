package com.example.resortbackendapplication1.contact.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunicationChannelDto {
    private Long id;
    private String code;
    private Integer sortOrder;
    private Boolean isUrl;
    private Boolean isPhone;
    private Boolean isEmail;
    private Boolean isClickable;
    private List<CommunicationChannelLocaleDto> locales;
}
