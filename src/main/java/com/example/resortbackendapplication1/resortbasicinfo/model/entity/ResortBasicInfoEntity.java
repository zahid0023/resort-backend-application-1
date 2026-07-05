package com.example.resortbackendapplication1.resortbasicinfo.model.entity;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "resort_basic_info")
public class ResortBasicInfoEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @Column(name = "estd", nullable = false)
    private Short estd;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity countryEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity cityEntity;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @Size(max = 500)
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @OneToMany(mappedBy = "resortBasicInfoEntity", cascade = CascadeType.ALL)
    private Set<ResortBasicInfoLocaleEntity> localeEntities = new LinkedHashSet<>();
}
