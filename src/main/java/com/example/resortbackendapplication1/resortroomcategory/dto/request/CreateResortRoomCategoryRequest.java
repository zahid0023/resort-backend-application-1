package com.example.resortbackendapplication1.resortroomcategory.dto.request;

import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryRequest extends ResortRoomCategoryRequest {

    @NotNull
    private Long roomCategoryId;

    @NotBlank
    @Size(max = 50)
    private String code;

    private List<CreateResortRoomCategoryLocaleRequest> locales;
}
