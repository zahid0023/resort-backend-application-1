package com.example.resortbackendapplication1.resort.dto.request.resortfacility;

import com.example.resortbackendapplication1.resort.dto.request.resortfacility.locale.ResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursDayScheduleRequest;
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
     * The facility's weekly operating-hours schedule — one entry per active day of week, no missing/duplicate
     * days. See {@link ResortFacilityOperatingHoursDayScheduleRequest}; same shape and validation as
     * {@code PUT .../operating-hours/schedule}.
     */
    @Valid
    @NotEmpty
    private List<@Valid ResortFacilityOperatingHoursDayScheduleRequest> operatingHours;
}
