package com.example.resortbackendapplication1.bedtype.dto.request.bedtype;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.CreateBedTypeLocaleRequest;
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
public class CreateBedTypeRequest extends BedTypeRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    private List<CreateBedTypeLocaleRequest> locales;
}
