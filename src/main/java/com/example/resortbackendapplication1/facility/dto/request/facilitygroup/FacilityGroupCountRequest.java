package com.example.resortbackendapplication1.facility.dto.request.facilitygroup;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class FacilityGroupCountRequest {

    @NotEmpty
    private Set<Long> facilityScopeIds;
}
