package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup;

import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacilitygroup.locale.ResortRoomCategoryFacilityGroupLocaleRequest;
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
public class CreateResortRoomCategoryFacilityGroupRequest extends ResortRoomCategoryFacilityGroupRequest {

    /** Optional link to a platform facility group. Null creates a resort-room-category-defined custom group. Immutable after creation. */
    private Long facilityGroupId;

    /** Resort-room-category-scoped identifier, unique per resort room category. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortRoomCategoryFacilityGroupLocaleRequest locale;
}
