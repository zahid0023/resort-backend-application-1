package com.example.resortbackendapplication1.commons.utils;

import java.util.function.Function;

public interface SearchFieldSpec<T> {
    String getFieldName();

    SearchType getSearchType();

    boolean isLocaleField();

    String getCollectionField();

    Function<T, String> getValueExtractor();
}
