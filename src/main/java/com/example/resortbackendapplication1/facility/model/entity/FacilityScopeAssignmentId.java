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
public class FacilityScopeAssignmentId implements Serializable {

    @Column(name = "facility_id")
    private Long facilityId;

    @Column(name = "facility_scope_id")
    private Long facilityScopeId;
}
