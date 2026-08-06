package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "facilities")
public class FacilityEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotBlank
    @Size(max = 100)
    @Column(name = "icon_type", nullable = false, length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "facilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityLocaleEntity> facilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityScopeAssignmentEntity> facilityScopeAssignmentEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityGroupFacilityAssignmentEntity> facilityGroupFacilityAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // Facility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityLocaleEntity(FacilityLocaleEntity entity) {
        addChild(facilityLocaleEntities, entity, FacilityLocaleEntity::assignFacility, this);
    }

    public void removeFacilityLocaleEntity(FacilityLocaleEntity entity) {
        removeChild(facilityLocaleEntities, entity, (child, ignored) -> child.unassignFacility());
    }

    // -------------------------------------------------------------------------
    // FacilityScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        addChild(facilityScopeAssignmentEntities, entity, FacilityScopeAssignmentEntity::assignFacility, this);
    }

    public void removeFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        removeChild(facilityScopeAssignmentEntities, entity, (child, ignored) -> child.unassignFacility());
    }

    // -------------------------------------------------------------------------
    // FacilityGroupFacilityAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityGroupFacilityAssignmentEntity(FacilityGroupFacilityAssignmentEntity entity) {
        addChild(facilityGroupFacilityAssignmentEntities, entity, FacilityGroupFacilityAssignmentEntity::assignFacility, this);
    }

    public void removeFacilityGroupFacilityAssignmentEntity(FacilityGroupFacilityAssignmentEntity entity) {
        removeChild(facilityGroupFacilityAssignmentEntities, entity, (child, ignored) -> child.unassignFacility());
    }
}
