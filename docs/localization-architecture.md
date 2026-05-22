# Localization Architecture

## Overview

The platform uses a **parent + locale table** pattern to store translatable content. Each localizable resource has a
dedicated locale table that stores one row per language. The `locales` table is the central registry of supported
languages — every locale table references it.

This approach keeps the parent entity schema clean (no language-specific columns) and allows any number of languages to
be added without schema changes.

---

## Table of Contents

1. [Locale Registry](#1-locale-registry)
2. [Locale Table Pattern](#2-locale-table-pattern)
3. [Localizable Resources](#3-localizable-resources)
4. [Database Schema](#4-database-schema)
5. [Java Layer](#5-java-layer)
6. [API Patterns](#6-api-patterns)
7. [Inline Creation](#7-inline-creation)
8. [Locale Sub-resource](#8-locale-sub-resource)
9. [Extending to New Resources](#9-extending-to-new-resources)

---

## 1. Locale Registry

The `locales` table is the single source of truth for all supported languages. Every locale entry in every locale table
has a foreign key into this table.

**Table: `locales`**

| Column       | Type         | Notes                                             |
|--------------|--------------|---------------------------------------------------|
| `id`         | bigserial PK | Auto-generated                                    |
| `code`       | varchar(50)  | Unique. BCP 47 / IETF tag (e.g. `en`, `bn`, `fr`) |
| `name`       | varchar(255) | Display name (e.g. `English`, `বাংলা`)            |
| `is_default` | boolean      | Marks the platform default locale                 |
| `sort_order` | int          | Display order                                     |
| *(audit)*    | —            | Standard audit columns from `AuditableEntity`     |

Locales are managed via `/api/v1/locales`. A locale must exist and be active before it can be used in any locale table.

---

## 2. Locale Table Pattern

Every localizable resource follows the same pattern:

```
┌─────────────────────┐         ┌───────────────────────────┐
│   parent_entity     │         │   parent_entity_locales   │
│                     │ 1     N │                           │
│  id  (PK)           │────────►│  id            (PK)       │
│  code               │         │  parent_id     (FK) ──────┘
│  sort_order         │         │  locale_id     (FK) ──► locales.id
│  ...                │         │  name                     │
│                     │         │  description              │
│                     │         │  sort_order               │
└─────────────────────┘         │  *(audit)*                │
                                └───────────────────────────┘
```

**Constraints applied to every locale table:**

| Constraint                          | Definition      | Purpose                                                    |
|-------------------------------------|-----------------|------------------------------------------------------------|
| `NOT NULL` on `parent_id`           | FK to parent    | A locale entry must belong to a parent                     |
| `NOT NULL` on `locale_id`           | FK to `locales` | A locale entry must target a known locale                  |
| `UNIQUE (parent_id, locale_id)`     | Composite       | One translation per locale per parent                      |
| `ON DELETE CASCADE` on `parent_id`  | FK              | Deleting a parent removes all its translations             |
| `ON DELETE RESTRICT` on `locale_id` | FK              | A locale cannot be deleted while translations reference it |

---

## 3. Localizable Resources

| Resource   | Parent Table | Locale Table        | API Base Path        |
|------------|--------------|---------------------|----------------------|
| Shop Types | `shop_types` | `shop_type_locales` | `/api/v1/shop-types` |
| Shop Roles | `shop_roles` | `shop_role_locales` | `/api/v1/shop-roles` |
| Countries  | `countries`  | `country_locales`   | `/api/v1/countries`  |

Each parent entity holds locale-neutral fields (`code`, `sort_order`, and resource-specific identifiers). All
human-readable text (`name`, `description`) lives exclusively in the locale table.

---

## 4. Database Schema

### Parent entities — locale-neutral fields only

```sql
-- shop_types
code        varchar(50)  NOT NULL UNIQUE
sort_order  int          NOT NULL DEFAULT 0

-- shop_roles
code        varchar(50)  NOT NULL UNIQUE
sort_order  int          NOT NULL DEFAULT 0

-- countries
code        varchar(10)  NOT NULL UNIQUE   -- ISO 3166-1 alpha-2
iso3_code   varchar(10)                   -- ISO 3166-1 alpha-3
phone_code  varchar(10)                   -- dialing code
sort_order  int          NOT NULL DEFAULT 0
```

### Locale tables — translatable fields

```sql
-- shop_type_locales
shop_type_id  bigint       NOT NULL REFERENCES shop_types(id) ON
DELETE CASCADE
    locale_id bigint NOT NULL REFERENCES locales(id)
ON
DELETE RESTRICT
    name varchar (150) NOT NULL
    description text NOT NULL
    sort_order int NOT NULL DEFAULT 0
    UNIQUE (shop_type_id, locale_id)

-- shop_role_locales
    shop_role_id bigint NOT NULL REFERENCES shop_roles(id)
ON
DELETE CASCADE
    locale_id bigint NOT NULL REFERENCES locales(id)
ON
DELETE RESTRICT
    name varchar (150) NOT NULL
    description text NOT NULL
    sort_order int NOT NULL DEFAULT 0
    UNIQUE (shop_role_id, locale_id)

-- country_locales
    country_id bigint NOT NULL REFERENCES countries(id)
ON
DELETE CASCADE
    locale_id bigint NOT NULL REFERENCES locales(id)
ON
DELETE RESTRICT
    name varchar (255) NOT NULL
    description text
    sort_order int NOT NULL DEFAULT 0
    UNIQUE (country_id, locale_id)
```

All tables inherit the standard audit columns from `AuditableEntity`: `created_by`, `created_at`, `updated_by`,
`updated_at`, `version`, `is_active`, `is_deleted`, `deleted_by`, `deleted_at`.

---

## 5. Java Layer

### Entity relationship

Every parent entity holds a `@OneToMany(cascade = CascadeType.ALL)` collection of its locale entities:

```java
// Example: ShopTypeEntity
@OneToMany(mappedBy = "shopTypeEntity", cascade = CascadeType.ALL)
private Set<ShopTypeLocaleEntity> shopTypeLocaleEntities = new LinkedHashSet<>();
```

Every locale entity holds two `@ManyToOne(fetch = FetchType.LAZY)` relationships — one to its parent and one to
`LocaleEntity`:

```java
// Example: ShopTypeLocaleEntity
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "shop_type_id", nullable = false)
private ShopTypeEntity shopTypeEntity;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "locale_id", nullable = false)
private LocaleEntity localeEntity;
```

Lazy fetching is used on both sides to avoid N+1 queries. Associations are only loaded when explicitly accessed.

### Mapper convention

Each locale table has a dedicated `@UtilityClass` mapper with three methods:

| Method                                             | Purpose                                                        |
|----------------------------------------------------|----------------------------------------------------------------|
| `fromRequest(request, parentEntity, localeEntity)` | Creates a new locale entity from a request DTO                 |
| `update(entity, request, localeEntity)`            | Applies changes from an update request onto an existing entity |
| `toDto(entity)`                                    | Projects an entity to its DTO for API responses                |

### Repository convention

Each locale repository extends `JpaRepository` and provides two derived query methods:

```java
// Fetch a single locale entry scoped to its parent
Optional<T> findByIdAndParentEntityAndIsActiveAndIsDeleted(
        Long id, ParentEntity parent, Boolean isActive, Boolean isDeleted);

// Fetch all locale entries for a parent (paginated)
Page<T> findAllByParentEntityAndIsActiveAndIsDeleted(
        ParentEntity parent, Boolean isActive, Boolean isDeleted, Pageable pageable);
```

Scoping queries by `parentEntity` ensures that a locale entry from one parent cannot be accessed via another parent's
URL path, preventing cross-resource data leakage.

### Service convention

Each locale service interface defines six methods:

```java
SuccessResponse create(ParentEntity parent, LocaleEntity locale, CreateRequest request);

ParentLocaleEntity getEntityById(Long id, ParentEntity parent);

ParentLocaleResponse getById(Long id, ParentEntity parent);

PaginatedResponse<ParentLocaleDto> getAll(ParentEntity parent, PaginatedRequest request);

SuccessResponse update(ParentLocaleEntity entity, LocaleEntity locale, UpdateRequest request);

SuccessResponse delete(ParentLocaleEntity entity);
```

---

## 6. API Patterns

### Request lifecycle for locale sub-resource endpoints

Every locale sub-resource endpoint follows the same three-step validation chain before executing business logic:

```
1. Resolve parent       countryService.getEntityById(countryId)
        │                   → 404 if not found or deleted
        ▼
2. Resolve locale       localeService.getEntityById(request.getLocaleId())
        │                   → 404 if not found or deleted
        ▼
3. Execute operation    countryLocaleService.create(country, locale, request)
                            → 409 if (country_id, locale_id) pair already exists
```

For get / update / delete, step 2 (locale resolution from path variable `{id}`) is scoped to the parent:

```
1. Resolve parent       countryService.getEntityById(countryId)
        │
        ▼
2. Resolve locale entry countryLocaleService.getEntityById(id, country)
                            → 404 if entry not found under this parent
```

This means a valid locale entry ID from country A will correctly return 404 when queried under country B's path.

### Soft-delete behaviour

Locale entries support independent soft-delete. Deleting a parent (`DELETE /api/v1/countries/{id}`) soft-deletes the
parent record only — the cascade in the service layer does not automatically soft-delete child locale entries. However,
because all queries filter on `is_active = true AND is_deleted = false`, soft-deleted parents are excluded from all
reads, making their locale entries effectively invisible.

---

## 7. Inline Creation

When creating a parent resource, locale translations can be embedded directly in the request body. The controller
resolves all locale IDs upfront and passes a pre-built `Map<Long, LocaleEntity>` to the mapper:

```
POST /api/v1/shop-types
        │
        ▼
Controller
  ├── Extract all locale_id values from request.getLocales()
  ├── localeService.getAll(localeIds)   → validates all IDs exist; throws 404 if any missing
  ├── Build Map<Long, LocaleEntity>
  └── shopTypeService.create(request, localeEntityMap)
              │
              ▼
        ShopTypeMapper.fromRequest(request, localeEntityMap)
          ├── Build ShopTypeEntity
          ├── For each locale in request: ShopTypeLocaleMapper.fromRequest(...)
          └── entity.setShopTypeLocaleEntities(localeSet)
              │
              ▼
        shopTypeRepository.save(entity)   ← cascades to locale table
```

If any `locale_id` in the array does not exist or is deleted, `EntityValidator.validateAllFound()` throws
`EntityNotFoundException` before the parent is created. The entire operation is wrapped in `@Transactional` so no
partial data is saved.

For resources where the service self-resolves locales (e.g., `CountryServiceImpl`), the same logic applies internally —
the controller simply passes the raw request.

---

## 8. Locale Sub-resource

After creation, individual locale entries are managed via a nested REST path:

```
/api/v1/{resource}/{resource-id}/locales
/api/v1/{resource}/{resource-id}/locales/{id}
```

| Method | Path                          | Description                                   |
|--------|-------------------------------|-----------------------------------------------|
| POST   | `/{resource-id}/locales`      | Add a new translation                         |
| GET    | `/{resource-id}/locales`      | List all translations (paginated)             |
| GET    | `/{resource-id}/locales/{id}` | Get a single translation                      |
| PUT    | `/{resource-id}/locales/{id}` | Replace a translation (can change the locale) |
| DELETE | `/{resource-id}/locales/{id}` | Soft-delete a translation                     |

Updating a locale entry allows changing the `locale_id`, which effectively re-assigns the translation to a different
language. The `UNIQUE (parent_id, locale_id)` constraint will reject the update if a translation for the target locale
already exists.

---

## 9. Extending to New Resources

To make a new entity localizable, follow this checklist:

**Database**

- [ ] Add `{resource}_locales` table with `{resource}_id FK`, `locale_id FK`, `name`, `description`, `sort_order`, all
  audit columns, and `UNIQUE ({resource}_id, locale_id)`

**Entity**

- [ ] Add `@OneToMany(mappedBy = "{resourceEntity}", cascade = CascadeType.ALL)` to the parent entity
- [ ] Create `{Resource}LocaleEntity` with `@ManyToOne` to both parent and `LocaleEntity`

**Application layer (per the convention)**

- [ ] `{Resource}LocaleDto` — response projection
- [ ] `{Resource}LocaleSortField` — enum of allowed sort fields
- [ ] `{Resource}LocaleRequest` / `Create` / `Update` — request DTOs
- [ ] `{Resource}LocaleResponse` — response wrapper
- [ ] `{Resource}LocaleMapper` — `fromRequest`, `update`, `toDto`
- [ ] `{Resource}LocaleRepository` — with the two scoped query methods
- [ ] `{Resource}LocaleService` + `{Resource}LocaleServiceImpl`
- [ ] `{Resource}LocaleController` at `/api/v1/{resources}/{resource-id}/locales`
