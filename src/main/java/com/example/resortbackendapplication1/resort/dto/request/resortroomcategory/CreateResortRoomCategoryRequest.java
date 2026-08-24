package com.example.resortbackendapplication1.resort.dto.request.resortroomcategory;

import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.ResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.CreateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceGroupRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    /** Platform room category to link. Immutable after creation. */
    @NotNull
    private Long roomCategoryId;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 50)
    private String code;

    @Valid
    @NotNull
    private ResortRoomCategoryLocaleRequest locale;

    @Valid
    @NotNull
    private CreateResortRoomCategoryMetaRequest meta;

    /** At least one bed configuration is required. Additional beds can be added afterward via POST /beds. */
    @Valid
    @NotEmpty
    private List<CreateResortRoomCategoryBedRequest> beds;

    /**
     * At least one currency's BASE/WEEKDAY/WEEKEND price set is required, one entry per currency.
     * Additional prices (including HOLIDAY/SPECIAL) can be added afterward via POST /prices.
     */
    @Valid
    @NotEmpty
    private List<CreateResortRoomCategoryPriceGroupRequest> prices;
}
