package com.example.resortbackendapplication1.mail.provider.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "mail_provider_config_fields")
public class MailProviderConfigFieldEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mail_provider_id", nullable = false)
    private MailProviderEntity mailProviderEntity;

    /** Internal — call via {@link MailProviderEntity#addMailProviderConfigFieldEntity}. */
    public void assignMailProvider(MailProviderEntity mailProviderEntity) {
        this.mailProviderEntity = mailProviderEntity;
    }

    /** Internal — call via {@link MailProviderEntity#removeMailProviderConfigFieldEntity}. */
    public void unassignMailProvider() {
        this.mailProviderEntity = null;
    }

    @NotBlank
    @Size(max = 100)
    @Column(name = "key", nullable = false, length = 100)
    private String key;

    @NotBlank
    @Size(max = 100)
    @Column(name = "label", nullable = false, length = 100)
    private String label;

    @NotBlank
    @Size(max = 30)
    @Column(name = "field_type", nullable = false, length = 30)
    private String fieldType;

    @NotNull
    @Column(name = "placeholder", nullable = false, length = 255)
    private String placeholder = "";

    @NotNull
    @Column(name = "default_value", nullable = false, length = 500)
    private String defaultValue = "";

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = true;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
