package com.example.resortbackendapplication1.pagetype.dto.request.pagetype;

import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePageTypeRequest extends PageTypeRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    private List<CreatePageTypeLocaleRequest> locales;
}
