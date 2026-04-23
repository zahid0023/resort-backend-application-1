package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.enums.IconType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "facilities")
public class FacilityEntity extends AuditableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_group_id", nullable = false)
    private FacilityGroupEntity facilityGroupEntity;

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "icon_type", nullable = false, length = 100)
    private IconType iconType;

    @Column(name = "icon_value", length = Integer.MAX_VALUE)
    private String iconValue;

    @Column(name = "icon_meta")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> iconMeta;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}