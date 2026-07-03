package com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection;

import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
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
public class CreateUiBlockSectionRequest extends UiBlockSectionRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    private List<CreateUiBlockSectionLocaleRequest> locales;
}
