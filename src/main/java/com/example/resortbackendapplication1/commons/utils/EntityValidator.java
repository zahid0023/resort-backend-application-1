package com.example.resortbackendapplication1.commons.utils;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityValidator {

    private EntityValidator() {
    }

    public static <K, T> void validateAllFound(Set<K> requestedIds, List<T> entities, Function<T, K> idExtractor, String entityName) {
        Set<K> foundIds = entities.stream().map(idExtractor).collect(Collectors.toSet());
        Set<K> missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException(entityName + " not found with ids: " + missingIds);
        }
    }
}
