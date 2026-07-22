package com.example.resortbackendapplication1.facility.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FacilityGroupScopeAssignmentId implements Serializable {

    @Column(name = "facility_group_id")
    private Long facilityGroupId;

    @Column(name = "facility_scope_id")
    private Long facilityScopeId;
}
