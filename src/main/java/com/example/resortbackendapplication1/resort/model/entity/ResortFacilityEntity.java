package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.commons.model.enums.IconType;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "resort_facilities")
public class ResortFacilityEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_facility_group_id", nullable = false)
    private ResortFacilityGroupEntity resortFacilityGroupEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private FacilityEntity facilityEntity;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @Column(name = "icon_type", length = 100)
    @Enumerated(EnumType.STRING)
    private IconType iconType;

    @Column(name = "icon_value", length = Integer.MAX_VALUE)
    private String iconValue;

    @Column(name = "icon_meta")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "resortFacilityEntity", cascade = CascadeType.ALL)
    private Set<ResortFacilityLocaleEntity> resortFacilityLocaleEntities = new LinkedHashSet<>();
}
