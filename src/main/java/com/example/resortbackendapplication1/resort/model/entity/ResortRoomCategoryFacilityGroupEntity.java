package com.example.resortbackendapplication1.resort.model.entity;

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
import lombok.AccessLevel;
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
@Table(name = "resort_room_category_facility_groups")
public class ResortRoomCategoryFacilityGroupEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategoryFacilityGroupEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategoryFacilityGroupEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    /** Optional link to a platform-defined facility group. Null means a resort-defined custom group. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_group_id")
    private FacilityGroupEntity facilityGroupEntity;

    /** Resort-room-category-scoped identifier, unique per resort room category. Immutable after creation. */
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

    @OneToMany(mappedBy = "resortRoomCategoryFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryFacilityGroupLocaleEntity> resortRoomCategoryFacilityGroupLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacilityGroupLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityGroupLocaleEntity(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        addChild(resortRoomCategoryFacilityGroupLocaleEntities, entity, ResortRoomCategoryFacilityGroupLocaleEntity::assignResortRoomCategoryFacilityGroup, this);
    }

    public void removeResortRoomCategoryFacilityGroupLocaleEntity(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        removeChild(resortRoomCategoryFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignResortRoomCategoryFacilityGroup());
    }

    @OneToMany(mappedBy = "resortRoomCategoryFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryFacilityEntity> resortRoomCategoryFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityEntity(ResortRoomCategoryFacilityEntity entity) {
        addChild(resortRoomCategoryFacilityEntities, entity, ResortRoomCategoryFacilityEntity::assignResortRoomCategoryFacilityGroup, this);
    }

    public void removeResortRoomCategoryFacilityEntity(ResortRoomCategoryFacilityEntity entity) {
        removeChild(resortRoomCategoryFacilityEntities, entity, (child, ignored) -> child.unassignResortRoomCategoryFacilityGroup());
    }
}
