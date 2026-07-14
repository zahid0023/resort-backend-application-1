package com.example.resortbackendapplication1.resort.model.projection;

import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ResortImageSummary {
    Long getId();

    @Value("#{target.resortEntity.id}")
    Long getResortId();

    String getExternalId();
    String getUrl();
    String getCaption();
    Integer getSortOrder();
}
