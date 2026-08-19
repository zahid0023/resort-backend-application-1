package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "facility_scopes")
public class FacilityScopeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "facilityScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityScopeLocaleEntity> facilityScopeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityGroupScopeAssignmentEntity> facilityGroupScopeAssignmentEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityScopeAssignmentEntity> facilityScopeAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // FacilityScope Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityScopeLocaleEntity(FacilityScopeLocaleEntity entity) {
        addChild(facilityScopeLocaleEntities, entity, FacilityScopeLocaleEntity::assignFacilityScope, this);
    }

    public void removeFacilityScopeLocaleEntity(FacilityScopeLocaleEntity entity) {
        removeChild(facilityScopeLocaleEntities, entity, (child, ignored) -> child.unassignFacilityScope());
    }

    // -------------------------------------------------------------------------
    // FacilityGroupScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityGroupScopeAssignmentEntity(FacilityGroupScopeAssignmentEntity entity) {
        addChild(facilityGroupScopeAssignmentEntities, entity, FacilityGroupScopeAssignmentEntity::assignFacilityScope, this);
    }

    public void removeFacilityGroupScopeAssignmentEntity(FacilityGroupScopeAssignmentEntity entity) {
        removeChild(facilityGroupScopeAssignmentEntities, entity, (child, ignored) -> child.unassignFacilityScope());
    }

    // -------------------------------------------------------------------------
    // FacilityScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        addChild(facilityScopeAssignmentEntities, entity, FacilityScopeAssignmentEntity::assignFacilityScope, this);
    }

    public void removeFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        removeChild(facilityScopeAssignmentEntities, entity, (child, ignored) -> child.unassignFacilityScope());
    }
}
