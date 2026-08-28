package com.example.resortbackendapplication1.resort.facility.model.entity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "resort_facility_groups")
public class ResortFacilityGroupEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Optional link to a platform-defined facility group. Null means a resort-defined custom group. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_group_id")
    private FacilityGroupEntity facilityGroupEntity;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @Size(max = 100)
    @Column(name = "icon_type", length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "resortFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortFacilityGroupLocaleEntity> resortFacilityGroupLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortFacilityGroupLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityGroupLocaleEntity(ResortFacilityGroupLocaleEntity entity) {
        addChild(resortFacilityGroupLocaleEntities, entity, ResortFacilityGroupLocaleEntity::assignResortFacilityGroup, this);
    }

    public void removeResortFacilityGroupLocaleEntity(ResortFacilityGroupLocaleEntity entity) {
        removeChild(resortFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignResortFacilityGroup());
    }

    @OneToMany(mappedBy = "resortFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortFacilityEntity> resortFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityEntity(ResortFacilityEntity entity) {
        addChild(resortFacilityEntities, entity, ResortFacilityEntity::assignResortFacilityGroup, this);
    }

    public void removeResortFacilityEntity(ResortFacilityEntity entity) {
        removeChild(resortFacilityEntities, entity, (child, ignored) -> child.unassignResortFacilityGroup());
    }
}
