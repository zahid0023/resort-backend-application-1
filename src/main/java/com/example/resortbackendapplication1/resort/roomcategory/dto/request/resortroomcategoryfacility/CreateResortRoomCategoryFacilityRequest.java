package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility;

import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.locale.ResortRoomCategoryFacilityLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryFacilityRequest extends ResortRoomCategoryFacilityRequest {

    /** Resort room category facility group this facility belongs to. Required. Immutable after creation. */
    @NotNull
    private Long resortRoomCategoryFacilityGroupId;

    /** Optional link to a platform facility. Null creates a resort-room-category-defined custom facility. Immutable after creation. */
    private Long facilityId;

    /** Resort-room-category-scoped identifier, unique per resort room category. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortRoomCategoryFacilityLocaleRequest locale;
}
