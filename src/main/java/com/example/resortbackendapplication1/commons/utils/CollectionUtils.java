package com.example.resortbackendapplication1.commons.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T, K> Set<K> extractIds(List<T> items, Function<T, K> idExtractor) {
        return items.stream().map(idExtractor).collect(Collectors.toSet());
    }
}
