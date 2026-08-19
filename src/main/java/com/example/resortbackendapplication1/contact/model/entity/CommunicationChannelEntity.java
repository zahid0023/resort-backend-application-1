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
@Table(name = "communication_channels")
public class CommunicationChannelEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_url", nullable = false)
    private Boolean isUrl = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_phone", nullable = false)
    private Boolean isPhone = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_email", nullable = false)
    private Boolean isEmail = false;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_clickable", nullable = false)
    private Boolean isClickable = true;

    @OneToMany(mappedBy = "communicationChannelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunicationChannelLocaleEntity> communicationChannelLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // CommunicationChannel Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addCommunicationChannelLocaleEntity(CommunicationChannelLocaleEntity entity) {
        addChild(communicationChannelLocaleEntities, entity, CommunicationChannelLocaleEntity::assignCommunicationChannel, this);
    }

    public void removeCommunicationChannelLocaleEntity(CommunicationChannelLocaleEntity entity) {
        removeChild(communicationChannelLocaleEntities, entity, (child, ignored) -> child.unassignCommunicationChannel());
    }
}
