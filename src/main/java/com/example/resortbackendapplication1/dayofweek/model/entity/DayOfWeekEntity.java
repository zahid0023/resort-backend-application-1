package com.example.resortbackendapplication1.dayofweek.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "days_of_week")
public class DayOfWeekEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotNull
    @Column(name = "iso_day_number", nullable = false)
    private Integer isoDayNumber;

    @NotNull
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @OneToMany(mappedBy = "dayOfWeekEntity", cascade = CascadeType.ALL)
    private Set<DayOfWeekLocaleEntity> dayOfWeekLocaleEntities = new LinkedHashSet<>();
}
