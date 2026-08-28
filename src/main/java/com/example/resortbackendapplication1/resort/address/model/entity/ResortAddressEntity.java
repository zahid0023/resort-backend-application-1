package com.example.resortbackendapplication1.resort.address.model.entity;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resort_addresses")
public class ResortAddressEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#assignResortAddressEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#unassignResortAddressEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity countryEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity cityEntity;

    @Size(max = 50)
    @Column(name = "postal_code", length = 50)
    private String postalCode;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @OneToMany(mappedBy = "resortAddressEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortAddressLocaleEntity> resortAddressLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortAddressLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortAddressLocaleEntity(ResortAddressLocaleEntity entity) {
        addChild(resortAddressLocaleEntities, entity, ResortAddressLocaleEntity::assignResortAddress, this);
    }

    public void removeResortAddressLocaleEntity(ResortAddressLocaleEntity entity) {
        removeChild(resortAddressLocaleEntities, entity, (child, ignored) -> child.unassignResortAddress());
    }
}
