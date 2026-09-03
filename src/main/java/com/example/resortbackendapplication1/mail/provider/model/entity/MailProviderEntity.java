package com.example.resortbackendapplication1.mail.provider.model.entity;

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
@Table(name = "mail_providers")
public class MailProviderEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description = "";

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "mailProviderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MailProviderConfigFieldEntity> configFieldEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "mailProviderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MailProviderConfigEntity> mailProviderConfigEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ConfigField relationship helpers
    // -------------------------------------------------------------------------

    public void addMailProviderConfigFieldEntity(MailProviderConfigFieldEntity entity) {
        addChild(configFieldEntities, entity, MailProviderConfigFieldEntity::assignMailProvider, this);
    }

    public void removeMailProviderConfigFieldEntity(MailProviderConfigFieldEntity entity) {
        removeChild(configFieldEntities, entity, (child, ignored) -> child.unassignMailProvider());
    }

    // -------------------------------------------------------------------------
    // MailProviderConfig relationship helpers
    // -------------------------------------------------------------------------

    public void addMailProviderConfigEntity(MailProviderConfigEntity entity) {
        addChild(mailProviderConfigEntities, entity, MailProviderConfigEntity::assignMailProvider, this);
    }

    public void removeMailProviderConfigEntity(MailProviderConfigEntity entity) {
        removeChild(mailProviderConfigEntities, entity, (child, ignored) -> child.unassignMailProvider());
    }
}
