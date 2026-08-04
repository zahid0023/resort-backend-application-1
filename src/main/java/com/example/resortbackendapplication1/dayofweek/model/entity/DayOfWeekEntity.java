package com.example.resortbackendapplication1.dayofweek.model.entity;

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

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

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
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "dayOfWeekEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DayOfWeekLocaleEntity> dayOfWeekLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // DayOfWeek Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addDayOfWeekLocaleEntity(DayOfWeekLocaleEntity entity) {
        addChild(dayOfWeekLocaleEntities, entity, DayOfWeekLocaleEntity::assignDayOfWeek, this);
    }

    public void removeDayOfWeekLocaleEntity(DayOfWeekLocaleEntity entity) {
        removeChild(dayOfWeekLocaleEntities, entity, (child, ignored) -> child.unassignDayOfWeek());
    }
}
