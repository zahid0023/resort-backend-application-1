package com.example.resortbackendapplication1.resort.dto.response.resortaddresses;

import com.example.resortbackendapplication1.resort.model.dto.ResortAddressDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAddressResponse {

    private final ResortAddressDto data;

    public ResortAddressResponse(ResortAddressDto data) {
        this.data = data;
    }
}
