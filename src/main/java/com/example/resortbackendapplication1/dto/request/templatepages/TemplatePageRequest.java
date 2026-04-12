package com.example.resortbackendapplication1.dto.request.templatepages;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TemplatePageRequest {
    private String pageName;
    private String pageSlug;
    private Integer pageOrder;
}
