package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility;

import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.locale.ResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursDayScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
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
public class CreateResortFacilityRequest extends ResortFacilityRequest {

    /** Optional link to a platform facility. Null creates a resort-defined custom facility. Immutable after creation. */
    private Long facilityId;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortFacilityLocaleRequest locale;

    /**
     * The facility's weekly operating-hours schedule. Most facilities don't have one — omit or leave empty to
     * create the facility with no schedule at all. When supplied, it must be complete: one entry per active day
     * of week, no missing/duplicate days. See {@link ResortFacilityOperatingHoursDayScheduleRequest}; same shape
     * and validation as {@code PUT .../operating-hours/schedule}.
     */
    @Valid
    private List<@Valid ResortFacilityOperatingHoursDayScheduleRequest> operatingHours;

    /**
     * Initial price for this facility. Most facilities have no price at all — a price is the exception, not
     * the rule — so omit to create the facility with no price. When supplied, it is created exactly like
     * {@code POST .../prices}; the resort facility itself is resolved from context, never from this field.
     */
    @Valid
    private CreateResortFacilityPriceRequest price;
}
