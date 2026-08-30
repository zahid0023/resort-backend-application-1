package com.example.resortbackendapplication1.resort.room.dto.request.resortroom;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.ResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
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
public class CreateResortRoomRequest extends ResortRoomRequest {

    /** Initial room status. Changed afterward only via PUT .../rooms/{id}/status, never via this resource's own update. */
    @NotNull
    private Long roomStatusId;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 50)
    private String code;

    @Valid
    @NotNull
    private ResortRoomLocaleRequest locale;

    /** Optional — if omitted, the room inherits its category's meta until its own is added via POST /meta. */
    @Valid
    private CreateResortRoomMetaRequest meta;

    /** Optional — if omitted, the room inherits its category's beds until its own are added via POST /beds. */
    @Valid
    private List<CreateResortRoomBedRequest> beds;

    /**
     * Optional, one entry per currency — same shape as
     * {@link com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.CreateResortRoomCategoryRequest#getPrices()}
     * so the frontend can reuse the same price-entry UI for both. If omitted (or a currency is missing from
     * this list), the room inherits that currency's price from its category until an override is added
     * afterward via POST /prices/main. Special prices are never created here — add them afterward via
     * POST /prices/specials.
     */
    @Valid
    private List<CreateResortRoomMainPriceRequest> prices;
}
