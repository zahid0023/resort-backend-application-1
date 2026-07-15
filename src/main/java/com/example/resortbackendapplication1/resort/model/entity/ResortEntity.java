package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resorts")
public class ResortEntity extends AuditableEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL)
    private Set<ResortUserEntity> resortUserEntities = new LinkedHashSet<>();

    @OneToOne(mappedBy = "resortEntity", cascade = CascadeType.PERSIST)
    private ResortBasicInfoEntity resortBasicInfoEntity;

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.PERSIST)
    private Set<ResortContactEntity> resortContactEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL)
    private Set<ResortFacilityGroupEntity> resortFacilityGroupEntities = new LinkedHashSet<>();
}
