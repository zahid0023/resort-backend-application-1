package com.example.resortbackendapplication1.bedtype.dto.response.bedtypes;

import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BedTypeResponse {
    private final BedTypeDto bedType;

    public BedTypeResponse(BedTypeDto bedType) {
        this.bedType = bedType;
    }
}
