package com.example.resortbackendapplication1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortImageDto {
    private Long id;
    private Long resortId;
    private String imageUrl;
    private String publicId;
    private String caption;
    private Boolean isDefault;
    private Integer sortOrder;
}
