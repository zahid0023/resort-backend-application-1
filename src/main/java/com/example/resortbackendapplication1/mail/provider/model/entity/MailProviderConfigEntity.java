package com.example.resortbackendapplication1.mail.provider.model.entity;

import com.example.resortbackendapplication1.commons.mail.MailConfigSource;
import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "mail_provider_configs")
public class MailProviderConfigEntity extends AuditableEntity implements MailConfigSource {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mail_provider_id", nullable = false)
    private MailProviderEntity mailProviderEntity;

    /** Internal — call via {@link MailProviderEntity#addMailProviderConfigEntity}. */
    public void assignMailProvider(MailProviderEntity mailProviderEntity) {
        this.mailProviderEntity = mailProviderEntity;
    }

    /** Internal — call via {@link MailProviderEntity#removeMailProviderConfigEntity}. */
    public void unassignMailProvider() {
        this.mailProviderEntity = null;
    }

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** Optional — only set on the config designated to back a given system flow. See {@link MailProviderConfigCode}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 100)
    private MailProviderConfigCode code;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> config;

    @Override
    public String getProviderCode() {
        return mailProviderEntity.getCode();
    }
}
