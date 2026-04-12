package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity extends AuditableEntity {

    @Size(max = 10)
    @Column(name = "code", length = 10)
    private String code;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

}