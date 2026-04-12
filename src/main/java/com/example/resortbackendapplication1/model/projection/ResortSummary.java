package com.example.resortbackendapplication1.model.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface ResortSummary {
    Long getId();

    UUID getUuid();

    String getName();

    String getDescription();

    String getAddress();

    String getContactEmail();

    String getContactPhone();

    @Value("#{target.countryEntity.id}")
    Long getCountryId();

    @Value("#{target.cityEntity?.id}")
    Long getCityId();
}
