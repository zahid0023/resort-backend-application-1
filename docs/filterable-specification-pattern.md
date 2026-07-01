# Filterable Specification Pattern

## Overview

This project uses a custom **Filterable Specification Pattern** to handle dynamic, multi-field filtering on paginated list endpoints. It is built on top of Spring Data JPA's `Specification` API and follows the **Open/Closed Principle** — adding a new filter field requires changing only the filter request class, with no changes to any specification, service, or utility class.

---

## Components

### 1. `Filterable` — `commons/utils/Filterable.java`

A contract interface that every filter request must implement.

```java
public interface Filterable {
    List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb);
}
```

**Role:** Forces each filter request to define *which fields it filters on* and *how*. This is the extension point — all filtering logic lives here, not in the specification or utility classes.

---

### 2. `SpecificationUtils` — `commons/utils/SpecificationUtils.java`

A shared utility class with three methods that never change.

```java
public <T> Specification<T> build(Filterable filterable)
public <T> void addActiveFilter(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb)
public <T> void addLikeFilter(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, String value)
```

| Method | What it does |
|---|---|
| `build(filterable)` | Creates a `Specification` that always applies the active/deleted guard, then delegates to the filterable for its field predicates |
| `addActiveFilter(...)` | Adds `isActive = true` AND `isDeleted = false` predicates — applied to every query automatically |
| `addLikeFilter(...)` | Adds a case-insensitive `LIKE '%value%'` predicate for a given field, but only if the value is non-null and non-blank |

**Role:** Central building block. `build` is the only entry point used by specification classes. The other two methods are helpers used inside `toPredicates` implementations.

---

### 3. Filter Request Classes

Each filterable list endpoint has its own filter request class that:
- Extends `PaginatedRequest` (inherits `page`, `size`, `sort_by`, `sort_dir`)
- Implements `Filterable`
- Declares optional filter fields as class fields
- Implements `toPredicates` to define the filtering logic for those fields

**Example — `CountryFilterRequest`:**

```java
public class CountryFilterRequest extends PaginatedRequest implements Filterable {
    private String code;
    private String iso3Code;
    private String phoneCode;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        SpecificationUtils.addLikeFilter(predicates, root, cb, "code", code);
        SpecificationUtils.addLikeFilter(predicates, root, cb, "iso3Code", iso3Code);
        SpecificationUtils.addLikeFilter(predicates, root, cb, "phoneCode", phoneCode);
        return predicates;
    }
}
```

**Example — `CityFilterRequest`:**

```java
public class CityFilterRequest extends PaginatedRequest implements Filterable {
    private String code;

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        SpecificationUtils.addLikeFilter(predicates, root, cb, "code", code);
        return predicates;
    }
}
```

**Role:** Owns the definition of what is filterable for a given entity. This is the *only* place that changes when a new filter field is added.

---

### 4. Specification Classes — `address/specification/`

Thin adapters that bridge the filter request to the JPA `Specification` type. They never change when new filter fields are added.

**`CountrySpecification`:**
```java
public Specification<CountryEntity> filter(CountryFilterRequest request) {
    return SpecificationUtils.build(request);
}
```

**`CitySpecification`:**
```java
public Specification<CityEntity> filter(Long countryId, CityFilterRequest request) {
    return SpecificationUtils.<CityEntity>build(request)
            .and((root, query, cb) -> cb.equal(root.get("countryEntity").get("id"), countryId));
}
```

`CitySpecification` composes an extra `countryId` predicate using Spring Data's `Specification.and()` because `countryId` comes from the URL path, not from a query parameter, so it cannot be part of the filter request itself.

**Role:** Bridge between the domain filter request and the JPA `Specification` API. Also responsible for composing any additional constraints that come from outside the filter request (e.g. path variables).

---

### 5. Repository

Each repository extends `JpaSpecificationExecutor<Entity>` in addition to `JpaRepository`, which provides the `findAll(Specification, Pageable)` method.

```java
public interface CountryRepository extends JpaRepository<CountryEntity, Long>,
        JpaSpecificationExecutor<CountryEntity> { ... }
```

---

### 6. Service Implementation

The service calls the specification and maps the resulting `Page<Entity>` to `Page<Dto>`:

```java
public PaginatedResponse<CountryDto> getAll(CountryFilterRequest request) {
    Page<CountryDto> page = countryRepository
            .findAll(CountrySpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
            .map(entity -> CountryMapper.toDto(entity, false));
    return Pagination.buildPaginatedResponse(page);
}
```

---

## Request Flow

```
HTTP GET /api/v1/countries?code=BD&iso3Code=BGD&page=0&size=10
        │
        ▼
CountryController
  → binds query params into CountryFilterRequest
        │
        ▼
CountryServiceImpl.getAll(CountryFilterRequest)
  → calls CountrySpecification.filter(request)
        │
        ▼
CountrySpecification.filter(request)
  → delegates to SpecificationUtils.build(request)
        │
        ▼
SpecificationUtils.build(request)
  → builds a Specification that:
      1. adds isActive = true
      2. adds isDeleted = false
      3. calls request.toPredicates(root, cb)
            │
            ▼
        CountryFilterRequest.toPredicates(root, cb)
          → adds LIKE predicate for "code" (if provided)
          → adds LIKE predicate for "iso3Code" (if provided)
          → adds LIKE predicate for "phoneCode" (if provided)
        │
        ▼
  → returns Specification (all predicates AND-ed)
        │
        ▼
countryRepository.findAll(specification, pageable)
  → executes: SELECT * FROM countries
              WHERE is_active = true
              AND is_deleted = false
              AND LOWER(code) LIKE '%bd%'         ← only if code was provided
              AND LOWER(iso3_code) LIKE '%bgd%'   ← only if iso3Code was provided
              ORDER BY id ASC
              LIMIT 10 OFFSET 0
        │
        ▼
Page<CountryEntity> → mapped to Page<CountryDto>
        │
        ▼
PaginatedResponse<CountryDto> returned to client
```

---

## How to Add a New Filter Field

To add a new searchable field (e.g. `timeZone`) to the Country list endpoint:

**Step 1 — Add the field to `CountryFilterRequest`:**
```java
public class CountryFilterRequest extends PaginatedRequest implements Filterable {
    private String code;
    private String iso3Code;
    private String phoneCode;
    private String timeZone; // ← new field

    @Override
    public List<Predicate> toPredicates(Root<?> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        SpecificationUtils.addLikeFilter(predicates, root, cb, "code", code);
        SpecificationUtils.addLikeFilter(predicates, root, cb, "iso3Code", iso3Code);
        SpecificationUtils.addLikeFilter(predicates, root, cb, "phoneCode", phoneCode);
        SpecificationUtils.addLikeFilter(predicates, root, cb, "timeZone", timeZone); // ← new line
        return predicates;
    }
}
```

**Step 2 — Done.** No changes needed anywhere else.

---

## How to Add a Non-String Filter (Exact Match)

For numeric or enum fields, add a new helper to `SpecificationUtils`:

```java
public <T> void addEqualFilter(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb,
                                String field, Object value) {
    if (value != null) {
        predicates.add(cb.equal(root.get(field), value));
    }
}
```

Then use it in the filter request's `toPredicates`:

```java
SpecificationUtils.addEqualFilter(predicates, root, cb, "sortOrder", sortOrder);
```

---

## Files Reference

| File | Package | Purpose |
|---|---|---|
| `Filterable.java` | `commons/utils` | Interface contract for filter requests |
| `SpecificationUtils.java` | `commons/utils` | Shared predicate helpers + `build` entry point |
| `CountryFilterRequest.java` | `address/dto/request/country` | Country filter fields + predicate logic |
| `CityFilterRequest.java` | `address/dto/request/city` | City filter fields + predicate logic |
| `CountrySpecification.java` | `address/specification` | Delegates to `SpecificationUtils.build` |
| `CitySpecification.java` | `address/specification` | Delegates to `SpecificationUtils.build` + composes `countryId` |
| `CountryRepository.java` | `address/repository` | Extends `JpaSpecificationExecutor` |
| `CityRepository.java` | `address/repository` | Extends `JpaSpecificationExecutor` |
