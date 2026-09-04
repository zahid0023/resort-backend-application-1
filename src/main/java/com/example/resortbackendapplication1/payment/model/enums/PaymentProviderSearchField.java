package com.example.resortbackendapplication1.payment.model.enums;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderFilterRequest;
import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * No field was selected as Filterable for PaymentProvider, so this enum has no constants — every list
 * request is unfiltered beyond pagination/sorting (mirrors PaymentMethodSearchField/PaymentStatusSearchField).
 */
@Getter
public enum PaymentProviderSearchField implements SearchFieldSpec<PaymentProviderFilterRequest> {
    ;

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PaymentProviderFilterRequest, String> valueExtractor;

    PaymentProviderSearchField(String fieldName, SearchType searchType, boolean localeField,
                               String collectionField,
                               Function<PaymentProviderFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PaymentProviderSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(PaymentProviderSearchField::isLocaleField)
                .map(PaymentProviderSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
