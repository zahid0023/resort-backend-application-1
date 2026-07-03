package com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory;

import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.CreateRoomCategoryLocaleRequest;
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
public class CreateRoomCategoryRequest extends RoomCategoryRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    private List<CreateRoomCategoryLocaleRequest> locales;
}
