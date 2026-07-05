package com.example.resortbackendapplication1.imagehosting.model.projection;

import com.example.resortbackendapplication1.imagehosting.enums.ImageHostingProvider;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ResortImageHostingConfigSummary {
    Long getId();
    String getName();
    ImageHostingProvider getProvider();
    Map<String, String> getConfig();
}
