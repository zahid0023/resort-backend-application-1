package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "resorts")
public class ResortEntity extends AuditableEntity {
    @NotNull
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity countryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;

    @Size(max = 255)
    @Column(name = "contact_email")
    private String contactEmail;

    @Size(max = 50)
    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserResortAccessEntity> userResortAccessesEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL)
    private Set<ResortImageEntity> resortImageEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL)
    private Set<ResortImageStorageConfigEntity> resortImageStorageConfigEntities = new LinkedHashSet<>();

}