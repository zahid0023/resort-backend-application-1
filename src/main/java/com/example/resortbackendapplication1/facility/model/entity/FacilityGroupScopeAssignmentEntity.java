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
@Table(name = "facility_group_scope_assignments")
public class FacilityGroupScopeAssignmentEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_group_id", nullable = false)
    private FacilityGroupEntity facilityGroupEntity;

    /** Internal — call via {@link FacilityGroupEntity#addFacilityGroupScopeAssignmentEntity}. */
    public void assignFacilityGroup(FacilityGroupEntity facilityGroupEntity) {
        this.facilityGroupEntity = facilityGroupEntity;
    }

    /** Internal — call via {@link FacilityGroupEntity#removeFacilityGroupScopeAssignmentEntity}. */
    public void unassignFacilityGroup() {
        this.facilityGroupEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_scope_id", nullable = false)
    private FacilityScopeEntity facilityScopeEntity;

    /** Internal — call via {@link FacilityScopeEntity#addFacilityGroupScopeAssignmentEntity}. */
    public void assignFacilityScope(FacilityScopeEntity facilityScopeEntity) {
        this.facilityScopeEntity = facilityScopeEntity;
    }

    /** Internal — call via {@link FacilityScopeEntity#removeFacilityGroupScopeAssignmentEntity}. */
    public void unassignFacilityScope() {
        this.facilityScopeEntity = null;
    }
}
