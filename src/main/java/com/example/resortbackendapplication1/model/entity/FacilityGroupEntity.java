package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
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

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "facility_groups")
public class FacilityGroupEntity extends AuditableEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "icon_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private IconType iconType;

    @Column(name = "icon_value", length = Integer.MAX_VALUE)
    private String iconValue;

    @Column(name = "icon_meta")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> iconMeta;

    @ColumnDefault("0")
    @Column(name = "sort_order")
    private Integer sortOrder;
}