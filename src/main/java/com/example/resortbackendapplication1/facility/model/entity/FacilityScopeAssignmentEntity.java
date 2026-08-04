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
@Table(name = "facility_scope_assignments")
public class FacilityScopeAssignmentEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_scope_id", nullable = false)
    private FacilityScopeEntity facilityScopeEntity;

    /** Internal — call via {@link FacilityScopeEntity#addFacilityScopeAssignmentEntity}. */
    public void assignFacilityScope(FacilityScopeEntity facilityScopeEntity) {
        this.facilityScopeEntity = facilityScopeEntity;
    }

    /** Internal — call via {@link FacilityScopeEntity#removeFacilityScopeAssignmentEntity}. */
    public void unassignFacilityScope() {
        this.facilityScopeEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private FacilityEntity facilityEntity;

    /** Internal — call via {@link FacilityEntity#addFacilityScopeAssignmentEntity}. */
    public void assignFacility(FacilityEntity facilityEntity) {
        this.facilityEntity = facilityEntity;
    }

    /** Internal — call via {@link FacilityEntity#removeFacilityScopeAssignmentEntity}. */
    public void unassignFacility() {
        this.facilityEntity = null;
    }
}
