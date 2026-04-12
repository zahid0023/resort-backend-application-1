package com.example.resortbackendapplication1.model.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CitySummary {
    Long getId();

    String getName();

    @Value("#{target.countryEntity.id}")
    Long getCountryId();
}
