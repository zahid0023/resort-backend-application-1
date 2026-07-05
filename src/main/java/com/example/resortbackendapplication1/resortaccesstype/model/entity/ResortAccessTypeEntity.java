package com.example.resortbackendapplication1.resortaccesstype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
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
@Table(name = "resort_access_types")
public class ResortAccessTypeEntity extends AuditableEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @OneToMany(mappedBy = "resortAccessTypeEntity", cascade = CascadeType.ALL)
    private Set<ResortAccessTypeLocaleEntity> resortAccessTypeLocaleEntities = new LinkedHashSet<>();
}
