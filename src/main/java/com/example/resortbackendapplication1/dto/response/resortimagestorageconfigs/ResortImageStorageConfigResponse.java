package com.example.resortbackendapplication1.dto.response.resortimagestorageconfigs;

import com.example.resortbackendapplication1.model.dto.ResortImageStorageConfigDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortImageStorageConfigResponse {
    private ResortImageStorageConfigDto data;

    public ResortImageStorageConfigResponse(ResortImageStorageConfigDto data) {
        this.data = data;
    }
}
