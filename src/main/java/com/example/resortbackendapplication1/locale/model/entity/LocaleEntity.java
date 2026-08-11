package com.example.resortbackendapplication1.locale.model.entity;

import com.example.resortbackendapplication1.address.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryLocaleEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyLocaleEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitLocaleEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitTypeLocaleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "locales")
public class LocaleEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "localeEntity")
    private Set<CountryLocaleEntity> countryLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<CityLocaleEntity> cityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<CurrencyLocaleEntity> currencyLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<UnitTypeLocaleEntity> unitTypeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<UnitLocaleEntity> unitLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<FacilityScopeLocaleEntity> facilityScopeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<FacilityGroupLocaleEntity> facilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<FacilityLocaleEntity> facilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<RoomCategoryLocaleEntity> roomCategoryLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PriceScopeLocaleEntity> priceScopeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PriceTypeLocaleEntity> priceTypeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PriceUnitLocaleEntity> priceUnitLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ContactTypeLocaleEntity> contactTypeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<CommunicationChannelLocaleEntity> communicationChannelLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<DayOfWeekLocaleEntity> dayOfWeekLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<BedTypeLocaleEntity> bedTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // Country Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addCountryLocaleEntity(CountryLocaleEntity entity) {
        addChild(countryLocaleEntities, entity, CountryLocaleEntity::assignLocale, this);
    }

    public void removeCountryLocaleEntity(CountryLocaleEntity entity) {
        removeChild(countryLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // City Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addCityLocaleEntity(CityLocaleEntity entity) {
        addChild(cityLocaleEntities, entity, CityLocaleEntity::assignLocale, this);
    }

    public void removeCityLocaleEntity(CityLocaleEntity entity) {
        removeChild(cityLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // Currency Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addCurrencyLocaleEntity(CurrencyLocaleEntity entity) {
        addChild(currencyLocaleEntities, entity, CurrencyLocaleEntity::assignLocale, this);
    }

    public void removeCurrencyLocaleEntity(CurrencyLocaleEntity entity) {
        removeChild(currencyLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // UnitType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addUnitTypeLocaleEntity(UnitTypeLocaleEntity entity) {
        addChild(unitTypeLocaleEntities, entity, UnitTypeLocaleEntity::assignLocale, this);
    }

    public void removeUnitTypeLocaleEntity(UnitTypeLocaleEntity entity) {
        removeChild(unitTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // Unit Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addUnitLocaleEntity(UnitLocaleEntity entity) {
        addChild(unitLocaleEntities, entity, UnitLocaleEntity::assignLocale, this);
    }

    public void removeUnitLocaleEntity(UnitLocaleEntity entity) {
        removeChild(unitLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // FacilityScope Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityScopeLocaleEntity(FacilityScopeLocaleEntity entity) {
        addChild(facilityScopeLocaleEntities, entity, FacilityScopeLocaleEntity::assignLocale, this);
    }

    public void removeFacilityScopeLocaleEntity(FacilityScopeLocaleEntity entity) {
        removeChild(facilityScopeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // FacilityGroup Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityGroupLocaleEntity(FacilityGroupLocaleEntity entity) {
        addChild(facilityGroupLocaleEntities, entity, FacilityGroupLocaleEntity::assignLocale, this);
    }

    public void removeFacilityGroupLocaleEntity(FacilityGroupLocaleEntity entity) {
        removeChild(facilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // Facility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityLocaleEntity(FacilityLocaleEntity entity) {
        addChild(facilityLocaleEntities, entity, FacilityLocaleEntity::assignLocale, this);
    }

    public void removeFacilityLocaleEntity(FacilityLocaleEntity entity) {
        removeChild(facilityLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // RoomCategory Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addRoomCategoryLocaleEntity(RoomCategoryLocaleEntity entity) {
        addChild(roomCategoryLocaleEntities, entity, RoomCategoryLocaleEntity::assignLocale, this);
    }

    public void removeRoomCategoryLocaleEntity(RoomCategoryLocaleEntity entity) {
        removeChild(roomCategoryLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PriceTypeScope Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceScopeLocaleEntity(PriceScopeLocaleEntity entity) {
        addChild(priceScopeLocaleEntities, entity, PriceScopeLocaleEntity::assignLocale, this);
    }

    public void removePriceScopeLocaleEntity(PriceScopeLocaleEntity entity) {
        removeChild(priceScopeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PriceType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceTypeLocaleEntity(PriceTypeLocaleEntity entity) {
        addChild(priceTypeLocaleEntities, entity, PriceTypeLocaleEntity::assignLocale, this);
    }

    public void removePriceTypeLocaleEntity(PriceTypeLocaleEntity entity) {
        removeChild(priceTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PriceUnit Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceUnitLocaleEntity(PriceUnitLocaleEntity entity) {
        addChild(priceUnitLocaleEntities, entity, PriceUnitLocaleEntity::assignLocale, this);
    }

    public void removePriceUnitLocaleEntity(PriceUnitLocaleEntity entity) {
        removeChild(priceUnitLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ContactType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addContactTypeLocaleEntity(ContactTypeLocaleEntity entity) {
        addChild(contactTypeLocaleEntities, entity, ContactTypeLocaleEntity::assignLocale, this);
    }

    public void removeContactTypeLocaleEntity(ContactTypeLocaleEntity entity) {
        removeChild(contactTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // CommunicationChannel Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addCommunicationChannelLocaleEntity(CommunicationChannelLocaleEntity entity) {
        addChild(communicationChannelLocaleEntities, entity, CommunicationChannelLocaleEntity::assignLocale, this);
    }

    public void removeCommunicationChannelLocaleEntity(CommunicationChannelLocaleEntity entity) {
        removeChild(communicationChannelLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // DayOfWeek Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addDayOfWeekLocaleEntity(DayOfWeekLocaleEntity entity) {
        addChild(dayOfWeekLocaleEntities, entity, DayOfWeekLocaleEntity::assignLocale, this);
    }

    public void removeDayOfWeekLocaleEntity(DayOfWeekLocaleEntity entity) {
        removeChild(dayOfWeekLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // BedType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addBedTypeLocaleEntity(BedTypeLocaleEntity entity) {
        addChild(bedTypeLocaleEntities, entity, BedTypeLocaleEntity::assignLocale, this);
    }

    public void removeBedTypeLocaleEntity(BedTypeLocaleEntity entity) {
        removeChild(bedTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

}
