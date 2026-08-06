package com.example.resortbackendapplication1.facility.dto.response.facilityscopes;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityScopeCountResponse {
    private final Long count;
    private final List<String> codes;

    public FacilityScopeCountResponse(final Long count, final List<String> codes) {
        this.count = count;
        this.codes = codes;
    }
}
