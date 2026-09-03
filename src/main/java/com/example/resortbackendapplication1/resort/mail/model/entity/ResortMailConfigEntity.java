package com.example.resortbackendapplication1.resort.mail.model.entity;

import com.example.resortbackendapplication1.commons.mail.MailConfigSource;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "resort_mail_configs")
public class ResortMailConfigEntity extends AuditableEntity implements MailConfigSource {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortMailConfigEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortMailConfigEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "mail_provider_id", nullable = false)
    private MailProviderEntity mailProviderEntity;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> config;

    @Override
    public String getProviderCode() {
        return mailProviderEntity.getCode();
    }
}
