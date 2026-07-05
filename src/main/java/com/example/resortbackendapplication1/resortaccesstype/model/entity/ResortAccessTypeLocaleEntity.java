package com.example.resortbackendapplication1.resortaccesstype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_access_type_locales")
public class ResortAccessTypeLocaleEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_access_type_id", nullable = false)
    private ResortAccessTypeEntity resortAccessTypeEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity localeEntity;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
