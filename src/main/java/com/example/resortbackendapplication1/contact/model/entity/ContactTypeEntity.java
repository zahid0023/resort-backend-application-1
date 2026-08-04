package com.example.resortbackendapplication1.contact.model.entity;

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
@Table(name = "contact_types")
public class ContactTypeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "contactTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactTypeLocaleEntity> contactTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ContactType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addContactTypeLocaleEntity(ContactTypeLocaleEntity entity) {
        addChild(contactTypeLocaleEntities, entity, ContactTypeLocaleEntity::assignContactType, this);
    }

    public void removeContactTypeLocaleEntity(ContactTypeLocaleEntity entity) {
        removeChild(contactTypeLocaleEntities, entity, (child, ignored) -> child.unassignContactType());
    }
}
