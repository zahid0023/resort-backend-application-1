package com.example.resortbackendapplication1.contact.dto.request;

import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
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
public class CreateContactTypeRequest extends ContactTypeRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    private List<CreateContactTypeLocaleRequest> locales;
}
