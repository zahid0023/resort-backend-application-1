package com.example.resortbackendapplication1.resort.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortImageDto {
    private Long id;
    private Long resortId;
    private Long configId;
    private String externalId;
    private String url;
    private String caption;
    private Integer sortOrder;
}
