package com.example.resortbackendapplication1.resortroletype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "resort_role_type_locales", uniqueConstraints = @UniqueConstraint(columnNames = {"resort_role_type_id", "locale_id"}))
public class ResortRoleTypeLocaleEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_role_type_id", nullable = false)
    private ResortRoleTypeEntity resortRoleTypeEntity;

    /** Internal — call via {@link ResortRoleTypeEntity#addResortRoleTypeLocaleEntity}. */
    public void assignResortRoleType(ResortRoleTypeEntity resortRoleTypeEntity) {
        this.resortRoleTypeEntity = resortRoleTypeEntity;
    }

    /** Internal — call via {@link ResortRoleTypeEntity#removeResortRoleTypeLocaleEntity}. */
    public void unassignResortRoleType() {
        this.resortRoleTypeEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity localeEntity;

    /** Internal — call via {@link LocaleEntity#addResortRoleTypeLocaleEntity}. */
    public void assignLocale(LocaleEntity localeEntity) {
        this.localeEntity = localeEntity;
    }

    /** Internal — call via {@link LocaleEntity#removeResortRoleTypeLocaleEntity}. */
    public void unassignLocale() {
        this.localeEntity = null;
    }

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description = "";

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
