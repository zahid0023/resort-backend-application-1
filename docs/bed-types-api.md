# Bed Types API

Base URL: `/api/v1/bed-types`

Bed types represent the kinds of beds a room can offer (e.g. `SINGLE`, `QUEEN`, `KING`), each identified by
a unique `code`. A bed type's display name and description are locale-specific and are managed through a
companion sub-resource — Bed Type Locales — reached via `/api/v1/bed-types/{bed-type-id}/locales`. The
platform ships with nine seeded bed types (`SINGLE`, `TWIN`, `DOUBLE`, `QUEEN`, `KING`, `SOFA`, `BUNK`,
`FUTON`, `MURPHY`), but unlike [Days of Week](days-of-week-api.md) this list is **not read-only** —
additional bed types can be created, and existing ones updated or deleted, through this API. All records
support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Bed Type)** and **`GET` (List/Search Bed Types)** — the header's value selects exactly
  one locale translation for the bed type's `locale` field: an exact match if the bed type has one,
  otherwise `en`, otherwise `null`.
- **`GET /{bed-type-id}/locales` (List Bed Type Locales)** — the header must be present, but its value has
  no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                            | Description                |
|--------|-------------------------------------------------|----------------------------|
| POST   | `/api/v1/bed-types`                             | Create a bed type          |
| GET    | `/api/v1/bed-types`                             | List / search bed types    |
| GET    | `/api/v1/bed-types/{id}`                        | Get a bed type             |
| PUT    | `/api/v1/bed-types/{id}`                        | Update a bed type          |
| DELETE | `/api/v1/bed-types/{id}`                        | Delete a bed type          |
| GET    | `/api/v1/bed-types/{bed-type-id}/locales`       | List a bed type's locales  |
| GET    | `/api/v1/bed-types/{bed-type-id}/locales/count` | Count a bed type's locales |
| POST   | `/api/v1/bed-types/{bed-type-id}/locales`       | Create a bed type locale   |
| PUT    | `/api/v1/bed-types/{bed-type-id}/locales/{id}`  | Update a bed type locale   |
| DELETE | `/api/v1/bed-types/{bed-type-id}/locales/{id}`  | Delete a bed type locale   |

---

## Data Model

### BedType

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                  |
|--------------|---------|----------|-----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                    |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code, e.g. `SINGLE`, `QUEEN`, `KING`                                                                                                |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                                |
| `locale`     | Object  | —        | nullable; see BedTypeLocale below                                     | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the bed type has no translations at all) |

### BedTypeLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 100 chars                                    | Localized display name, e.g. `King Bed`                                        |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Bed Type

`POST /api/v1/bed-types`

Creates a new bed type together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted bed types — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Bed Type Locales sub-resource below.

### Request Body

```json
{
  "code": "CALIFORNIA_KING",
  "sort_order": 10,
  "locale": {
    "name": "California King Bed",
    "description": "An extra-long, extra-wide bed, typically 183 x 213 cm.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                                 |
|--------------|---------|----------|--------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars, unique among active records                                       |
| `sort_order` | Integer | Yes      | Not null                                                                                   |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

## Get Bed Type

`GET /api/v1/bed-types/{id}`

Returns a single active bed type by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the bed type has no translations at all). To
fetch every translation a bed type has, use [List Bed Type Locales](#list-bed-type-locales) below.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the bed type |

### Response `200 OK`

```json
{
  "data": {
    "id": 5,
    "code": "KING",
    "sort_order": 5,
    "locale": {
      "id": 5,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "King Bed",
      "description": "The largest standard bed size, typically 180 x 200 cm.",
      "sort_order": 5
    }
  }
}
```

---

## List / Search Bed Types

`GET /api/v1/bed-types`

Returns a paginated, filterable list of active (non-deleted) bed types. All filter parameters are optional;
omitting them returns all bed types. Multiple filters are combined with AND. Each `LIKE`-type filter
performs a case-insensitive partial match. `Accept-Language` selects each bed type's `locale` field the
same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `BedTypeFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming`
> (which produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute` /
> `@ParameterObject` query-string binding goes through Spring's plain `DataBinder` instead, which
> matches the exact property name.

| Parameter | Type   | Default         | Constraints                                       | Description                                                                               |
|-----------|--------|-----------------|---------------------------------------------------|-------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                                 | Filter by internal code (partial, case-insensitive), e.g. `KING`                          |
| `name`    | String | —               | —                                                 | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`    | int    | `0`             | >= 0                                              | Zero-based page index                                                                     |
| `size`    | int    | `10`            | 1 – 50                                            | Number of items per page                                                                  |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                          |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                     | Sort direction                                                                            |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "SINGLE",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Single Bed",
        "description": "A bed designed for one person, typically 90 x 190 cm.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "TWIN",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Twin Bed",
        "description": "Two separate single beds in the same room, each typically 90 x 190 cm.",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 9,
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

## Update Bed Type

`PUT /api/v1/bed-types/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Bed Type Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the bed type |

### Request Body

```json
{
  "sort_order": 6
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
  "id": 5
}
```

---

## Delete Bed Type

`DELETE /api/v1/bed-types/{id}`

Soft-deletes the bed type. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the bed type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 5
}
```

---

## Bed Type Locales

Bed Type Locale endpoints manage locale-specific name/description translations for a bed type. The
`{bed-type-id}` path parameter must reference an existing, active bed type.

---

### List Bed Type Locales

`GET /api/v1/bed-types/{bed-type-id}/locales`

Returns a paginated list of every locale translation belonging to a bed type — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET /bed-types/{id}` and
`GET /bed-types`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `bed-type-id` | Long | ID of the parent bed type |

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
      "id": 5,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "King Bed",
      "description": "The largest standard bed size, typically 180 x 200 cm.",
      "sort_order": 5
    },
    {
      "id": 14,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "কিং বেড",
      "description": "",
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

### Count Bed Type Locales

`GET /api/v1/bed-types/{bed-type-id}/locales/count`

Returns how many active locale translations a bed type currently has, plus the `code` of each one. Compare
this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active locale codes)
to determine which languages the bed type is still missing and can add a translation for via
[Create Bed Type Locale](#create-bed-type-locale) — e.g. if the platform has `en`, `bn`, `es` and this
endpoint returns `en` for the bed type, `bn` and `es` are still available; if it returns all three, every
platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `bed-type-id` | Long | ID of the parent bed type |

#### Response `200 OK`

```json
{
  "count": 1,
  "codes": [
    "en"
  ]
}
```

---

### Create Bed Type Locale

`POST /api/v1/bed-types/{bed-type-id}/locales`

Adds a new locale translation to an existing bed type. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of bed type and locale must
be unique — adding a locale the bed type already has a translation for returns `409 CONFLICT`, pre-checked
at the application level before any write (backed by a DB-level unique constraint (`uq_bed_type_locale` on
`bed_type_id` + `locale_id`) as a last-resort guard).

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `bed-type-id` | Long | ID of the parent bed type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "কিং বেড",
  "description": "",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|---------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 100 chars                    |
| `description` | String  | Yes      | Not null                                    |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 14
}
```

---

### Update Bed Type Locale

`PUT /api/v1/bed-types/{bed-type-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing bed type locale translation. The associated
bed type and locale cannot be changed after creation.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `bed-type-id` | Long | ID of the parent bed type |
| `id`          | Long | ID of the bed type locale |

#### Request Body

```json
{
  "name": "কিং বেড",
  "description": "",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 14
}
```

---

### Delete Bed Type Locale

`DELETE /api/v1/bed-types/{bed-type-id}/locales/{id}`

Soft-deletes a bed type locale. The record is not removed from the database but will no longer appear in
any response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `bed-type-id` | Long | ID of the parent bed type |
| `id`          | Long | ID of the bed type locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 14
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
  "message": "BedType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                             |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value             |
| 404         | `ENTITY_NOT_FOUND`         | Bed type not found, bed type locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                |
| 409         | `CONFLICT`                 | `code` already in use by another active bed type (`create`); or the bed type already has a translation for the given `locale_id` (`create` bed type locale, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint (`uq_bed_type_locale`) on `bed_type_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level     |
