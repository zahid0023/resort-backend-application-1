package com.example.resortbackendapplication1.resortpermissiontype.model.entity;

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
@Table(name = "resort_permission_types")
public class ResortPermissionTypeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "resortPermissionTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortPermissionTypeLocaleEntity> resortPermissionTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortPermissionType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortPermissionTypeLocaleEntity(ResortPermissionTypeLocaleEntity entity) {
        addChild(resortPermissionTypeLocaleEntities, entity, ResortPermissionTypeLocaleEntity::assignResortPermissionType, this);
    }

    public void removeResortPermissionTypeLocaleEntity(ResortPermissionTypeLocaleEntity entity) {
        removeChild(resortPermissionTypeLocaleEntities, entity, (child, ignored) -> child.unassignResortPermissionType());
    }
}
