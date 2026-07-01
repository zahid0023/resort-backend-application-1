package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.commons.model.enums.IconType;
import jakarta.persistence.*;
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

@Getter
@Setter
@Entity
@Table(name = "facility_groups")
public class FacilityGroupEntity extends AuditableEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @Column(name = "icon_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private IconType iconType;

    @Column(name = "icon_value", length = Integer.MAX_VALUE)
    private String iconValue;

    @Column(name = "icon_meta")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> iconMeta;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @OneToMany(mappedBy = "facilityGroupEntity", cascade = CascadeType.ALL)
    private Set<FacilityGroupLocaleEntity> facilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityGroupEntity")
    private Set<FacilityEntity> facilityEntities = new LinkedHashSet<>();
}
