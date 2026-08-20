package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "resort_contacts")
public class ResortContactEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortContactEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortContactEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactTypeEntity contactTypeEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "communication_channel_id", nullable = false)
    private CommunicationChannelEntity communicationChannelEntity;

    @NotBlank
    @Column(name = "contact_value", nullable = false, columnDefinition = "text")
    private String contactValue;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
