package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resort_users")
public class ResortUserEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_access_type_id", nullable = false)
    private ResortAccessTypeEntity resortAccessTypeEntity;

    @NotNull
    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @OneToMany(mappedBy = "resortUserEntity", cascade = CascadeType.ALL)
    private Set<ResortUserPermissionEntity> resortUserPermissionEntities = new LinkedHashSet<>();
}
