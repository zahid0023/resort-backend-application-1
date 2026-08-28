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
@Table(name = "resort_room_category_locales")
public class ResortRoomCategoryLocaleEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategoryLocaleEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategoryLocaleEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity localeEntity;

    /** Internal — call via {@link LocaleEntity#addResortRoomCategoryLocaleEntity}. */
    public void assignLocale(LocaleEntity localeEntity) {
        this.localeEntity = localeEntity;
    }

    /** Internal — call via {@link LocaleEntity#removeResortRoomCategoryLocaleEntity}. */
    public void unassignLocale() {
        this.localeEntity = null;
    }

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description = "";

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
