package com.example.resortbackendapplication1.resortpermissiontype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resort_permission_types")
public class ResortPermissionTypeEntity extends AuditableEntity {

    @Size(max = 150)
    @NotNull
    @Column(name = "code", nullable = false, length = 150)
    private String code;

    @NotNull
    @Min(1)
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @OneToMany(mappedBy = "resortPermissionTypeEntity", cascade = CascadeType.ALL)
    private Set<ResortPermissionTypeLocaleEntity> resortPermissionTypeLocaleEntities = new LinkedHashSet<>();
}
