package com.example.resortbackendapplication1.resortroletype.model.entity;

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
@Table(name = "resort_role_types")
public class ResortRoleTypeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @OneToMany(mappedBy = "resortRoleTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoleTypeLocaleEntity> resortRoleTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoleType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoleTypeLocaleEntity(ResortRoleTypeLocaleEntity entity) {
        addChild(resortRoleTypeLocaleEntities, entity, ResortRoleTypeLocaleEntity::assignResortRoleType, this);
    }

    public void removeResortRoleTypeLocaleEntity(ResortRoleTypeLocaleEntity entity) {
        removeChild(resortRoleTypeLocaleEntities, entity, (child, ignored) -> child.unassignResortRoleType());
    }
}
