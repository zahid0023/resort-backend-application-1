package com.example.resortbackendapplication1.resort.core.model.entity;

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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "resort_basic_info_locales")
public class ResortBasicInfoLocaleEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resort_basic_info_id", nullable = false)
    private ResortBasicInfoEntity resortBasicInfoEntity;

    /** Internal — call via {@link ResortBasicInfoEntity#addResortBasicInfoLocaleEntity}. */
    public void assignResortBasicInfo(ResortBasicInfoEntity resortBasicInfoEntity) {
        this.resortBasicInfoEntity = resortBasicInfoEntity;
    }

    /** Internal — call via {@link ResortBasicInfoEntity#removeResortBasicInfoLocaleEntity}. */
    public void unassignResortBasicInfo() {
        this.resortBasicInfoEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity localeEntity;

    /** Internal — call via {@link LocaleEntity#addResortBasicInfoLocaleEntity}. */
    public void assignLocale(LocaleEntity localeEntity) {
        this.localeEntity = localeEntity;
    }

    /** Internal — call via {@link LocaleEntity#removeResortBasicInfoLocaleEntity}. */
    public void unassignLocale() {
        this.localeEntity = null;
    }

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "tagline", nullable = false, length = Integer.MAX_VALUE)
    private String tagline;

    @Size(max = 1024)
    @Column(name = "short_description")
    private String shortDescription;
}
