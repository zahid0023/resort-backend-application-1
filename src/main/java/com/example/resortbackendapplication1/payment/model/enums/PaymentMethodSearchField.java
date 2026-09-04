package com.example.resortbackendapplication1.payment.model.enums;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodFilterRequest;
import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * No field was selected as Filterable for PaymentMethod, so this enum has no constants — every list request
 * is unfiltered beyond pagination/sorting. Kept (rather than removed) so PaymentMethodFilterRequest/
 * PaymentMethodSpecification can still mirror the standard Filterable wiring used by every other entity.
 */
@Getter
public enum PaymentMethodSearchField implements SearchFieldSpec<PaymentMethodFilterRequest> {
    ;

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PaymentMethodFilterRequest, String> valueExtractor;

    PaymentMethodSearchField(String fieldName, SearchType searchType, boolean localeField,
                             String collectionField,
                             Function<PaymentMethodFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PaymentMethodSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(PaymentMethodSearchField::isLocaleField)
                .map(PaymentMethodSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
