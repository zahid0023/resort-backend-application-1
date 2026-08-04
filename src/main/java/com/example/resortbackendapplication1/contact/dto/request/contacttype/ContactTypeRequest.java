package com.example.resortbackendapplication1.contact.dto.request.contacttype;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactTypeRequest {

    @NotNull
    private Integer sortOrder;

}
