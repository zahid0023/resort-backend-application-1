package com.example.resortbackendapplication1.resortpermissiontype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_permission_type_locales")
public class ResortPermissionTypeLocaleEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_permission_type_id", nullable = false)
    private ResortPermissionTypeEntity resortPermissionTypeEntity;

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
    @Min(1)
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
