package com.example.resortbackendapplication1.model.projection;

import com.example.resortbackendapplication1.enums.IconType;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface FacilitySummary {
    Long getId();
    String getCode();
    Integer getSortOrder();
    IconType getIconType();
    String getIconValue();

    @Value("#{target.iconMeta}")
    Map<String, Object> getIconMeta();

    @Value("#{target.facilityGroupEntity.id}")
    Long getFacilityGroupId();

    @Value("#{target.facilityLocaleEntities}")
    List<? extends LocaleSummary> getLocales();

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    interface LocaleSummary {
        Long getId();

        @Value("#{target.localeEntity.id}")
        Long getLocaleId();

        String getName();
        String getDescription();
        Integer getSortOrder();
    }
}
