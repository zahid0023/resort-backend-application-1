package com.example.resortbackendapplication1.locale.model.projection;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface LocaleSummary {
    Long getId();
    String getCode();
    String getName();
    Integer getSortOrder();
}
