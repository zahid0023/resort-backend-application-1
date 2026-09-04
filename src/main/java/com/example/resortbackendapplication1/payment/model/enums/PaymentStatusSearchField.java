package com.example.resortbackendapplication1.payment.model.enums;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusFilterRequest;
import com.example.resortbackendapplication1.commons.utils.SearchFieldSpec;
import com.example.resortbackendapplication1.commons.utils.SearchType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * No field was selected as Filterable for PaymentStatus, so this enum has no constants — every list
 * request is unfiltered beyond pagination/sorting (mirrors PaymentMethodSearchField).
 */
@Getter
public enum PaymentStatusSearchField implements SearchFieldSpec<PaymentStatusFilterRequest> {
    ;

    private final String fieldName;
    private final SearchType searchType;
    private final boolean localeField;
    private final String collectionField;
    private final Function<PaymentStatusFilterRequest, String> valueExtractor;

    PaymentStatusSearchField(String fieldName, SearchType searchType, boolean localeField,
                             String collectionField,
                             Function<PaymentStatusFilterRequest, String> valueExtractor) {
        this.fieldName = fieldName;
        this.searchType = searchType;
        this.localeField = localeField;
        this.collectionField = collectionField;
        this.valueExtractor = valueExtractor;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(PaymentStatusSearchField::getFieldName)
                .collect(Collectors.toSet());
    }

    public static Set<String> localeSearchFields() {
        return Arrays.stream(values())
                .filter(PaymentStatusSearchField::isLocaleField)
                .map(PaymentStatusSearchField::getFieldName)
                .collect(Collectors.toSet());
    }
}
