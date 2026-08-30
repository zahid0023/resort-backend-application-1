package com.example.resortbackendapplication1.contact.model.entity;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "user_phones")
public class UserPhoneEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    /** Internal — call via {@link UserEntity#addUserPhoneEntity}. */
    public void assignUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    /** Internal — call via {@link UserEntity#removeUserPhoneEntity}. */
    public void unassignUser() {
        this.userEntity = null;
    }

    @NotBlank
    @Size(max = 50)
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_whatsapp", nullable = false)
    private Boolean isWhatsapp = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
