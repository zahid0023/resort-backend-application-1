package com.example.resortbackendapplication1.locale.model.entity;

import com.example.resortbackendapplication1.address.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryLocaleEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyLocaleEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressLocaleEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
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
    @Column(name = "code", nullable = false, length = 50)
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
    private Set<FacilityPriceTypeLocaleEntity> facilityPriceTypeLocaleEntities = new LinkedHashSet<>();

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

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoleTypeLocaleEntity> resortRoleTypeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortPermissionTypeLocaleEntity> resortPermissionTypeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortBasicInfoLocaleEntity> resortBasicInfoLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortAddressLocaleEntity> resortAddressLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortFacilityGroupLocaleEntity> resortFacilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortFacilityLocaleEntity> resortFacilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomCategoryLocaleEntity> resortRoomCategoryLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomLocaleEntity> resortRoomLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomCategoryFacilityGroupLocaleEntity> resortRoomCategoryFacilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomCategoryFacilityLocaleEntity> resortRoomCategoryFacilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomFacilityGroupLocaleEntity> resortRoomFacilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ResortRoomFacilityLocaleEntity> resortRoomFacilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<RoomStatusLocaleEntity> roomStatusLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<BookingSourceLocaleEntity> bookingSourceLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<ReservationStatusLocaleEntity> reservationStatusLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PaymentMethodLocaleEntity> paymentMethodLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PaymentStatusLocaleEntity> paymentStatusLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "localeEntity")
    private Set<PaymentProviderLocaleEntity> paymentProviderLocaleEntities = new LinkedHashSet<>();

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
    // FacilityPriceType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityPriceTypeLocaleEntity(FacilityPriceTypeLocaleEntity entity) {
        addChild(facilityPriceTypeLocaleEntities, entity, FacilityPriceTypeLocaleEntity::assignLocale, this);
    }

    public void removeFacilityPriceTypeLocaleEntity(FacilityPriceTypeLocaleEntity entity) {
        removeChild(facilityPriceTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
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

    // -------------------------------------------------------------------------
    // ResortRoleType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoleTypeLocaleEntity(ResortRoleTypeLocaleEntity entity) {
        addChild(resortRoleTypeLocaleEntities, entity, ResortRoleTypeLocaleEntity::assignLocale, this);
    }

    public void removeResortRoleTypeLocaleEntity(ResortRoleTypeLocaleEntity entity) {
        removeChild(resortRoleTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortPermissionType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortPermissionTypeLocaleEntity(ResortPermissionTypeLocaleEntity entity) {
        addChild(resortPermissionTypeLocaleEntities, entity, ResortPermissionTypeLocaleEntity::assignLocale, this);
    }

    public void removeResortPermissionTypeLocaleEntity(ResortPermissionTypeLocaleEntity entity) {
        removeChild(resortPermissionTypeLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortBasicInfo Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortBasicInfoLocaleEntity(ResortBasicInfoLocaleEntity entity) {
        addChild(resortBasicInfoLocaleEntities, entity, ResortBasicInfoLocaleEntity::assignLocale, this);
    }

    public void removeResortBasicInfoLocaleEntity(ResortBasicInfoLocaleEntity entity) {
        removeChild(resortBasicInfoLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortAddress Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortAddressLocaleEntity(ResortAddressLocaleEntity entity) {
        addChild(resortAddressLocaleEntities, entity, ResortAddressLocaleEntity::assignLocale, this);
    }

    public void removeResortAddressLocaleEntity(ResortAddressLocaleEntity entity) {
        removeChild(resortAddressLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortFacilityGroup Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityGroupLocaleEntity(ResortFacilityGroupLocaleEntity entity) {
        addChild(resortFacilityGroupLocaleEntities, entity, ResortFacilityGroupLocaleEntity::assignLocale, this);
    }

    public void removeResortFacilityGroupLocaleEntity(ResortFacilityGroupLocaleEntity entity) {
        removeChild(resortFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortFacility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityLocaleEntity(ResortFacilityLocaleEntity entity) {
        addChild(resortFacilityLocaleEntities, entity, ResortFacilityLocaleEntity::assignLocale, this);
    }

    public void removeResortFacilityLocaleEntity(ResortFacilityLocaleEntity entity) {
        removeChild(resortFacilityLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortRoomCategory Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryLocaleEntity(ResortRoomCategoryLocaleEntity entity) {
        addChild(resortRoomCategoryLocaleEntities, entity, ResortRoomCategoryLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomCategoryLocaleEntity(ResortRoomCategoryLocaleEntity entity) {
        removeChild(resortRoomCategoryLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    public void addResortRoomLocaleEntity(ResortRoomLocaleEntity entity) {
        addChild(resortRoomLocaleEntities, entity, ResortRoomLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomLocaleEntity(ResortRoomLocaleEntity entity) {
        removeChild(resortRoomLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacilityGroup Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityGroupLocaleEntity(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        addChild(resortRoomCategoryFacilityGroupLocaleEntities, entity, ResortRoomCategoryFacilityGroupLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomCategoryFacilityGroupLocaleEntity(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        removeChild(resortRoomCategoryFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityLocaleEntity(ResortRoomCategoryFacilityLocaleEntity entity) {
        addChild(resortRoomCategoryFacilityLocaleEntities, entity, ResortRoomCategoryFacilityLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomCategoryFacilityLocaleEntity(ResortRoomCategoryFacilityLocaleEntity entity) {
        removeChild(resortRoomCategoryFacilityLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortRoomFacilityGroup Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityGroupLocaleEntity(ResortRoomFacilityGroupLocaleEntity entity) {
        addChild(resortRoomFacilityGroupLocaleEntities, entity, ResortRoomFacilityGroupLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomFacilityGroupLocaleEntity(ResortRoomFacilityGroupLocaleEntity entity) {
        removeChild(resortRoomFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ResortRoomFacility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityLocaleEntity(ResortRoomFacilityLocaleEntity entity) {
        addChild(resortRoomFacilityLocaleEntities, entity, ResortRoomFacilityLocaleEntity::assignLocale, this);
    }

    public void removeResortRoomFacilityLocaleEntity(ResortRoomFacilityLocaleEntity entity) {
        removeChild(resortRoomFacilityLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // RoomStatus Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addRoomStatusLocaleEntity(RoomStatusLocaleEntity entity) {
        addChild(roomStatusLocaleEntities, entity, RoomStatusLocaleEntity::assignLocale, this);
    }

    public void removeRoomStatusLocaleEntity(RoomStatusLocaleEntity entity) {
        removeChild(roomStatusLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ReservationSource Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addBookingSourceLocaleEntity(BookingSourceLocaleEntity entity) {
        addChild(bookingSourceLocaleEntities, entity, BookingSourceLocaleEntity::assignLocale, this);
    }

    public void removeBookingSourceLocaleEntity(BookingSourceLocaleEntity entity) {
        removeChild(bookingSourceLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // ReservationStatus Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addReservationStatusLocaleEntity(ReservationStatusLocaleEntity entity) {
        addChild(reservationStatusLocaleEntities, entity, ReservationStatusLocaleEntity::assignLocale, this);
    }

    public void removeReservationStatusLocaleEntity(ReservationStatusLocaleEntity entity) {
        removeChild(reservationStatusLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PaymentMethod Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentMethodLocaleEntity(PaymentMethodLocaleEntity entity) {
        addChild(paymentMethodLocaleEntities, entity, PaymentMethodLocaleEntity::assignLocale, this);
    }

    public void removePaymentMethodLocaleEntity(PaymentMethodLocaleEntity entity) {
        removeChild(paymentMethodLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PaymentStatus Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentStatusLocaleEntity(PaymentStatusLocaleEntity entity) {
        addChild(paymentStatusLocaleEntities, entity, PaymentStatusLocaleEntity::assignLocale, this);
    }

    public void removePaymentStatusLocaleEntity(PaymentStatusLocaleEntity entity) {
        removeChild(paymentStatusLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

    // -------------------------------------------------------------------------
    // PaymentProvider Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentProviderLocaleEntity(PaymentProviderLocaleEntity entity) {
        addChild(paymentProviderLocaleEntities, entity, PaymentProviderLocaleEntity::assignLocale, this);
    }

    public void removePaymentProviderLocaleEntity(PaymentProviderLocaleEntity entity) {
        removeChild(paymentProviderLocaleEntities, entity, (child, ignored) -> child.unassignLocale());
    }

}
