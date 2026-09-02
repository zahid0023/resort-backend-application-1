package com.example.resortbackendapplication1.auth.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;
import com.example.resortbackendapplication1.contact.model.entity.UserPhoneEntity;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends AuditableEntity {
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity roleEntity;

    @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @ColumnDefault("false")
    @Column(name = "locked", nullable = false)
    private Boolean locked = true;

    @NotNull
    @ColumnDefault("false") 
    @Column(name = "expired", nullable = false)
    private Boolean expired = false;

    @OneToMany(mappedBy = "userEntity")
    private Set<UserPermissionEntity> userPermissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserEmailEntity> userEmailEntities = new LinkedHashSet<>();

    public void addUserEmailEntity(UserEmailEntity entity) {
        addChild(userEmailEntities, entity, UserEmailEntity::assignUser, this);
    }

    public void removeUserEmailEntity(UserEmailEntity entity) {
        removeChild(userEmailEntities, entity, (child, ignored) -> child.unassignUser());
    }

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPhoneEntity> userPhoneEntities = new LinkedHashSet<>();

    public void addUserPhoneEntity(UserPhoneEntity entity) {
        addChild(userPhoneEntities, entity, UserPhoneEntity::assignUser, this);
    }

    public void removeUserPhoneEntity(UserPhoneEntity entity) {
        removeChild(userPhoneEntities, entity, (child, ignored) -> child.unassignUser());
    }

    /** No cascade — a booking is a historical record of a customer, not owned by it. */
    @OneToMany(mappedBy = "userEntity")
    private Set<ResortBookingEntity> resortBookingEntities = new LinkedHashSet<>();

    public void addResortBookingEntity(ResortBookingEntity entity) {
        addChild(resortBookingEntities, entity, ResortBookingEntity::assignUser, this);
    }

    public void removeResortBookingEntity(ResortBookingEntity entity) {
        removeChild(resortBookingEntities, entity, (child, ignored) -> child.unassignUser());
    }
}