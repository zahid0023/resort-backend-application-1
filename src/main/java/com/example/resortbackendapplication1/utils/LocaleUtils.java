package com.example.resortbackendapplication1.utils;

import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.service.LocaleService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocaleUtils {

    public static <T> Map<Long, LocaleEntity> resolveLocaleMap(
            List<T> localeRequests,
            Function<T, Long> localeIdExtractor,
            LocaleService localeService) {
        if (localeRequests == null || localeRequests.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> localeIds = localeRequests.stream()
                .map(localeIdExtractor)
                .collect(Collectors.toSet());
        return localeService.getAll(localeIds).stream()
                .collect(Collectors.toMap(LocaleEntity::getId, l -> l));
    }
}
