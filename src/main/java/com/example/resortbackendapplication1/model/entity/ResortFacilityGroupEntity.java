package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resort_facility_group")
public class ResortFacilityGroupEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_group_id", nullable = false)
    private FacilityGroupEntity facilityGroupEntity;

    @Size(max = 255)
    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    @NotNull
    private String description;

    @Column(name = "sort_order")
    @NotNull
    private Integer sortOrder;
}