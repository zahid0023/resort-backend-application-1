package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "facility_group_facility_assignments")
public class FacilityGroupFacilityAssignmentEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_group_id", nullable = false)
    private FacilityGroupEntity facilityGroupEntity;

    /** Internal — call via {@link FacilityGroupEntity#addFacilityGroupFacilityAssignmentEntity}. */
    public void assignFacilityGroup(FacilityGroupEntity facilityGroupEntity) {
        this.facilityGroupEntity = facilityGroupEntity;
    }

    /** Internal — call via {@link FacilityGroupEntity#removeFacilityGroupFacilityAssignmentEntity}. */
    public void unassignFacilityGroup() {
        this.facilityGroupEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private FacilityEntity facilityEntity;

    /** Internal — call via {@link FacilityEntity#addFacilityGroupFacilityAssignmentEntity}. */
    public void assignFacility(FacilityEntity facilityEntity) {
        this.facilityEntity = facilityEntity;
    }

    /** Internal — call via {@link FacilityEntity#removeFacilityGroupFacilityAssignmentEntity}. */
    public void unassignFacility() {
        this.facilityEntity = null;
    }
}
