package com.example.resortbackendapplication1.utils;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityLookup {

    public static <T> Map<Long, T> validateAndMap(Set<Long> requestedIds,
                                                  List<T> foundEntities,
                                                  Function<T, Long> idExtractor,
                                                  String entityName) {
        Map<Long, T> map = foundEntities.stream().collect(Collectors.toMap(idExtractor, e -> e));
        Set<Long> missingIds = requestedIds.stream().filter(id -> !map.containsKey(id)).collect(Collectors.toSet());
        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException(entityName + "s not found for ids: " + missingIds);
        }
        return map;
    }
}
