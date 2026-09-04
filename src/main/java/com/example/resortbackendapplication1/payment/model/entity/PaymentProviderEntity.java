package com.example.resortbackendapplication1.payment.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "payment_providers")
public class PaymentProviderEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethodEntity paymentMethodEntity;

    /** Internal — call via {@link PaymentMethodEntity#addPaymentProviderEntity}. */
    public void assignPaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        this.paymentMethodEntity = paymentMethodEntity;
    }

    /** Internal — call via {@link PaymentMethodEntity#removePaymentProviderEntity}. */
    public void unassignPaymentMethod() {
        this.paymentMethodEntity = null;
    }

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "paymentProviderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentProviderLocaleEntity> paymentProviderLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PaymentProvider Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentProviderLocaleEntity(PaymentProviderLocaleEntity entity) {
        addChild(paymentProviderLocaleEntities, entity, PaymentProviderLocaleEntity::assignPaymentProvider, this);
    }

    public void removePaymentProviderLocaleEntity(PaymentProviderLocaleEntity entity) {
        removeChild(paymentProviderLocaleEntities, entity, (child, ignored) -> child.unassignPaymentProvider());
    }
}
