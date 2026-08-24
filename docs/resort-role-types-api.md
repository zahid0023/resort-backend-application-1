# Resort Role Types API

Base URL: `/api/v1/resort-role-types`

Resort role types define the roles a user can hold with respect to a resort (e.g. `OWNER`, `BOOKER`), each
identified by a unique `code`. A resort role type's display name and description are locale-specific and are
managed through a companion sub-resource — Resort Role Type Locales — reached via
`/api/v1/resort-role-types/{resort-role-type-id}/locales`. All records support soft-delete — deleted records
are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Resort Role Type)** and **`GET` (List/Search Resort Role Types)** — the header's value
  selects exactly one locale translation for the resort role type's `locale` field: an exact match if the
  resort role type has one, otherwise `en`, otherwise `null`.
- **`GET /{resort-role-type-id}/locales` (List Resort Role Type Locales)** — the header must be present, but
  its value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not
  a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                           | Description                       |
|--------|----------------------------------------------------------------|-----------------------------------|
| POST   | `/api/v1/resort-role-types`                                    | Create a resort role type         |
| GET    | `/api/v1/resort-role-types`                                    | List / search resort role types   |
| GET    | `/api/v1/resort-role-types/{id}`                               | Get a resort role type            |
| PUT    | `/api/v1/resort-role-types/{id}`                               | Update a resort role type         |
| DELETE | `/api/v1/resort-role-types/{id}`                               | Delete a resort role type         |
| GET    | `/api/v1/resort-role-types/{resort-role-type-id}/locales`      | List a resort role type's locales |
| GET    | `/api/v1/resort-role-types/{resort-role-type-id}/locales/count` | Count a resort role type's used platform locales |
| POST   | `/api/v1/resort-role-types/{resort-role-type-id}/locales`      | Create a resort role type locale  |
| PUT    | `/api/v1/resort-role-types/{resort-role-type-id}/locales/{id}` | Update a resort role type locale  |
| DELETE | `/api/v1/resort-role-types/{resort-role-type-id}/locales/{id}` | Delete a resort role type locale  |

---

## Data Model

### ResortRoleType

| Field        | Type    | Required | Constraints                                                            | Description                                                                                                                                          |
|--------------|---------|----------|------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                              | Auto-generated identifier                                                                                                                            |
| `code`       | String  | Yes      | max 100 chars, unique among active records; set at creation, immutable | Internal code (e.g. `OWNER`, `BOOKER`)                                                                                                               |
| `sort_order` | Integer | Yes      | default 1                                                              | Display order                                                                                                                                        |
| `locale`     | Object  | —        | nullable; see ResortRoleTypeLocale below                               | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the resort role type has no translations at all) |

### ResortRoleTypeLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 255 chars                                    | Localized name of the role                                                     |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 1                                        | Display order among locale entries                                             |

---

## Create Resort Role Type

`POST /api/v1/resort-role-types`

Creates a new resort role type together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted resort role types — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional
languages are added afterward via the Resort Role Type Locales sub-resource below.

### Request Body

```json
{
  "code": "OWNER",
  "sort_order": 1,
  "locale": {
    "name": "Owner",
    "description": "Full control over the resort — can manage settings, staff, and bookings.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                                 |
|--------------|---------|----------|--------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars, unique among active records                                      |
| `sort_order` | Integer | Yes      | Not null                                                                                   |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Role Type

`GET /api/v1/resort-role-types/{id}`

Returns a single active resort role type by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the resort role type has no translations at
all). To fetch every translation a resort role type has, use
[List Resort Role Type Locales](#list-resort-role-type-locales) below.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the resort role type |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "OWNER",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Owner",
      "description": "Full control over the resort — can manage settings, staff, and bookings.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Resort Role Types

`GET /api/v1/resort-role-types`

Returns a paginated, filterable list of active (non-deleted) resort role types. All filter parameters are
optional; omitting them returns all resort role types. Multiple filters are combined with AND. Each
`LIKE`-type filter performs a case-insensitive partial match. `Accept-Language` selects each resort role
type's `locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortRoleTypeFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                                       | Description                                                                               |
|-----------|--------|-----------------|---------------------------------------------------|-------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                                 | Filter by code (partial, case-insensitive)                                                |
| `name`    | String | —               | —                                                 | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`    | int    | `0`             | >= 0                                              | Zero-based page index                                                                     |
| `size`    | int    | `10`            | 1 – 50                                            | Number of items per page                                                                  |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                          |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                     | Sort direction                                                                            |

> **Note:** `sort_order` is not filterable or sortable — only `code` and locale `name` are wired into the
> search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "OWNER",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Owner",
        "description": "Full control over the resort — can manage settings, staff, and bookings.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "BOOKER",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Booker",
        "description": "Can browse resort details and make reservations on behalf of guests.",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "code",
    "name"
  ],
  "searchable_fields": [
    "code",
    "name"
  ]
}
```

---

## Update Resort Role Type

`PUT /api/v1/resort-role-types/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Resort Role Type Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the resort role type |

### Request Body

```json
{
  "sort_order": 2
}
```

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `sort_order` | Integer | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Role Type

`DELETE /api/v1/resort-role-types/{id}`

Soft-deletes the resort role type. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the resort role type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Role Type Locales

Resort Role Type Locale endpoints manage locale-specific name/description translations for a resort role
type. The `{resort-role-type-id}` path parameter must reference an existing, active resort role type.

---

### List Resort Role Type Locales

`GET /api/v1/resort-role-types/{resort-role-type-id}/locales`

Returns a paginated list of every locale translation belonging to a resort role type — this is the only way
to see more than the single Accept-Language-matched translation returned by
`GET /resort-role-types/{id}` and `GET /resort-role-types`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `resort-role-type-id` | Long | ID of the parent resort role type |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                           |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                        |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Owner",
      "description": "Full control over the resort — can manage settings, staff, and bookings.",
      "sort_order": 1
    },
    {
      "id": 3,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "মালিক",
      "description": "রিসোর্টের উপর সম্পূর্ণ নিয়ন্ত্রণ — সেটিংস, কর্মী এবং বুকিং পরিচালনা করতে পারেন।",
      "sort_order": 1
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

### Count Resort Role Type Locales

`GET /api/v1/resort-role-types/{resort-role-type-id}/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes this resort role type already
has an active translation for, together with each one's `code`. Matched via `locale_id`. `count` is always
`codes.length`. Use this to gray out/disable locales already present in `codes` when building the picker for
[Create Resort Role Type Locale](#create-resort-role-type-locale) — `locale_id` must not already have a
translation for this resort role type, or the create call returns `409 CONFLICT`.

#### Path Parameters

| Parameter             | Type | Description                       |
|------------------------|------|--------------------------------------|
| `resort-role-type-id` | Long | ID of the parent resort role type |

#### Response `200 OK`

```json
{
  "count": 2,
  "codes": [
    "en",
    "bn"
  ]
}
```

---

### Create Resort Role Type Locale

`POST /api/v1/resort-role-types/{resort-role-type-id}/locales`

Adds a new locale translation to an existing resort role type. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort role type
and locale must be unique — adding a locale the resort role type already has a translation for returns
`409 CONFLICT`. `name` must also be unique among active translations for the same locale, regardless of
which resort role type they belong to — reusing a name already in use for that locale returns
`409 CONFLICT`. Both checks are application-level only; the underlying `resort_role_type_locales` table has
no DB-level unique constraints backing either one (aside from the `(resort_role_type_id, locale_id)`
uniqueness pair).

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `resort-role-type-id` | Long | ID of the parent resort role type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "মালিক",
  "description": "রিসোর্টের উপর সম্পূর্ণ নিয়ন্ত্রণ — সেটিংস, কর্মী এবং বুকিং পরিচালনা করতে পারেন।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                 |
|---------------|---------|----------|----------------------------------------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale                                |
| `name`        | String  | Yes      | Not blank, max 255 chars, unique among active translations for `locale_id` |
| `description` | String  | Yes      | Not null                                                                   |
| `sort_order`  | Integer | Yes      | Not null                                                                   |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Resort Role Type Locale

`PUT /api/v1/resort-role-types/{resort-role-type-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing resort role type locale translation. The
associated resort role type and locale cannot be changed after creation. `name` is re-checked for
uniqueness among active translations for the same locale, excluding this translation itself — renaming it
to a name already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `resort-role-type-id` | Long | ID of the parent resort role type |
| `id`                  | Long | ID of the resort role type locale |

#### Request Body

```json
{
  "name": "মালিক",
  "description": "রিসোর্টের উপর সম্পূর্ণ নিয়ন্ত্রণ — সেটিংস, কর্মী এবং বুকিং পরিচালনা করতে পারেন।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                 |
|---------------|---------|----------|----------------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars, unique among active translations for this locale |
| `description` | String  | Yes      | Not null                                                                   |
| `sort_order`  | Integer | Yes      | Not null                                                                   |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Resort Role Type Locale

`DELETE /api/v1/resort-role-types/{resort-role-type-id}/locales/{id}`

Soft-deletes a resort role type locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `resort-role-type-id` | Long | ID of the parent resort role type |
| `id`                  | Long | ID of the resort role type locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "ResortRoleType not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                                                                                                                            |
|-------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                                            |
| 404         | `ENTITY_NOT_FOUND` | Resort role type not found, resort role type locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                                                                                               |
| 409         | `CONFLICT`         | `code` already in use by another active resort role type (`create`); the resort role type already has a translation for the given `locale_id` (`create` locale, pre-checked); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
