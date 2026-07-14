package com.example.resortbackendapplication1.resort.model.projection;

import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ResortUserSummary {
    Long getId();

    @Value("#{target.resortEntity.id}")
    Long getResortId();

    @Value("#{target.userEntity.id}")
    Long getUserId();

    @Value("#{target.resortAccessTypeEntity.id}")
    Long getResortAccessTypeId();

    Instant getJoinedAt();
}
