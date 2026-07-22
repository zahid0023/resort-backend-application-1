package com.example.resortbackendapplication1.resortroomcategory.dto.request;

import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.meta.ResortRoomCategoryMetaRequest;
import jakarta.validation.Valid;
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

    @NotNull
    @Valid
    private ResortRoomCategoryMetaRequest meta;

    @Valid
    private List<ResortRoomCategoryBedRequest> beds;

    @Valid
    private List<CreateResortRoomCategoryLocaleRequest> locales;
}
