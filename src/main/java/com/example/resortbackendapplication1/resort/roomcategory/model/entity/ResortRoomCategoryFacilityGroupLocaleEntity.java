package com.example.resortbackendapplication1.resort.roomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_facility_group_locales")
public class ResortRoomCategoryFacilityGroupLocaleEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_facility_group_id", nullable = false)
    private ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity;

    /** Internal — call via {@link ResortRoomCategoryFacilityGroupEntity#addResortRoomCategoryFacilityGroupLocaleEntity}. */
    public void assignResortRoomCategoryFacilityGroup(ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity) {
        this.resortRoomCategoryFacilityGroupEntity = resortRoomCategoryFacilityGroupEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryFacilityGroupEntity#removeResortRoomCategoryFacilityGroupLocaleEntity}. */
    public void unassignResortRoomCategoryFacilityGroup() {
        this.resortRoomCategoryFacilityGroupEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity localeEntity;

    /** Internal — call via {@link LocaleEntity#addResortRoomCategoryFacilityGroupLocaleEntity}. */
    public void assignLocale(LocaleEntity localeEntity) {
        this.localeEntity = localeEntity;
    }

    /** Internal — call via {@link LocaleEntity#removeResortRoomCategoryFacilityGroupLocaleEntity}. */
    public void unassignLocale() {
        this.localeEntity = null;
    }

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description = "";

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
