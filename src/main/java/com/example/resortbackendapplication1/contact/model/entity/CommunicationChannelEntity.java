package com.example.resortbackendapplication1.contact.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
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
@Table(name = "communication_channels")
public class CommunicationChannelEntity extends AuditableEntity {

    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, unique = true, length = 50)
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

    @OneToMany(mappedBy = "communicationChannelEntity", cascade = CascadeType.ALL)
    private Set<CommunicationChannelLocaleEntity> communicationChannelLocaleEntities = new LinkedHashSet<>();
}
