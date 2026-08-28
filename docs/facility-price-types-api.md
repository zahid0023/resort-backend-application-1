# Facility Price Types API

Base URL: `/api/v1/facility-price-types`

Facility price types classify how a [resort facility price](resort-facility-prices-api.md) is billed —
`FREE`, `INCLUDED`, `FIXED`, or `VARIABLE`. This is a small, single-purpose classification: unlike price units
or currencies, it is scoped to exactly one thing (resort facility pricing) and is never shared with room
category/room pricing, which has its own dedicated main/special price tables instead of a generic type
column. Each price type is identified by a unique `code`. A price type's display name, description, and
administrative guidance are locale-specific and are managed through a companion sub-resource — Resort
Facility Price Type Locales — reached via `/api/v1/facility-price-types/{facility-price-type-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Facility Price Type)** and **`GET` (List/Search Facility Price Types)** —
  the header's value selects exactly one locale translation for the price type's `locale` field: an exact
  match if the price type has one, otherwise `en`, otherwise `null`.
  - **`GET /{facility-price-type-id}/locales` (List Facility Price Type Locales)** — the header
    must be present, but its value has no effect; this endpoint returns every translation (optionally filtered
    by `localeCode`), not a single Accept-Language-matched one.
  - **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                  | Description                           |
|--------|-----------------------------------------------------------------------|---------------------------------------|
| POST   | `/api/v1/facility-price-types`                                        | Create a facility price type          |
| GET    | `/api/v1/facility-price-types`                                        | List / search facility price types    |
| GET    | `/api/v1/facility-price-types/{id}`                                   | Get a facility price type             |
| PUT    | `/api/v1/facility-price-types/{id}`                                   | Update a facility price type          |
| DELETE | `/api/v1/facility-price-types/{id}`                                   | Delete a facility price type          |
| GET    | `/api/v1/facility-price-types/{facility-price-type-id}/locales`       | List a facility price type's locales  |
| GET    | `/api/v1/facility-price-types/{facility-price-type-id}/locales/count` | Count a facility price type's locales |
| POST   | `/api/v1/facility-price-types/{facility-price-type-id}/locales`       | Create a facility price type locale   |
| PUT    | `/api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`  | Update a facility price type locale   |
| DELETE | `/api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`  | Delete a facility price type locale   |

---

## Data Model

### FacilityPriceType

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                    |
|--------------|---------|----------|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                      |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (`FREE`, `INCLUDED`, `FIXED`, `VARIABLE`)                                                                                        |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                                  |
| `locale`     | Object  | —        | nullable; see FacilityPriceTypeLocale below                           | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the price type has no translations at all) |

### FacilityPriceTypeLocale

| Field           | Type    | Required | Constraints                                                         | Description                                                                    |
|-----------------|---------|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                           | Auto-generated identifier                                                      |
| `locale`        | Locale  | —        | read-only, resolved from `locale_id` at creation                    | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`          | String  | Yes      | max 100 chars, unique among active translations for the same locale | Localized name of the price type                                               |
| `description`   | String  | Yes      | not null (defaults to `""`)                                         | Localized description                                                          |
| `sort_order`    | Integer | Yes      | default 0                                                           | Display order among locale entries                                             |
| `purpose`       | String  | Yes      | not null (defaults to `""`)                                         | Localized explanation of why this price type exists / when to use it           |
| `usage_example` | String  | Yes      | not null (defaults to `""`)                                         | Localized example scenario shown to administrators                             |

---

## Create Facility Price Type

`POST /api/v1/facility-price-types`

Creates a new facility price type together with exactly **one** initial locale translation. `code`
must be unique among active, non-deleted price types — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Facility Price Type Locales sub-resource below.

### Request Body

```json
{
  "code": "FIXED",
  "sort_order": 3,
  "locale": {
    "name": "Fixed",
    "description": "A flat, fixed amount is charged for using the facility, per the selected price unit.",
    "sort_order": 3,
    "purpose": "Standard flat-rate billing for a facility.",
    "usage_example": "The gym costs a FIXED $10 per day."
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

| Field           | Type    | Required | Validation                                                          |
|-----------------|---------|----------|---------------------------------------------------------------------|
| `name`          | String  | Yes      | Not blank, max 100 chars, unique among active translations for `en` |
| `description`   | String  | Yes      | Not null                                                            |
| `sort_order`    | Integer | Yes      | Not null                                                            |
| `purpose`       | String  | Yes      | Not null                                                            |
| `usage_example` | String  | Yes      | Not null                                                            |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Facility Price Type

`GET /api/v1/facility-price-types/{id}`

Returns a single active price type by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the price type has no translations at all). To
fetch every translation a price type has, use [List Facility Price Type
Locales](#list-facility-price-type-locales) below.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Response `200 OK`

```json
{
  "data": {
    "id": 3,
    "code": "FIXED",
    "sort_order": 3,
    "locale": {
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Fixed",
      "description": "A flat, fixed amount is charged for using the facility, per the selected price unit.",
      "sort_order": 3,
      "purpose": "Standard flat-rate billing for a facility.",
      "usage_example": "The gym costs a FIXED $10 per day."
    }
  }
}
```

---

## List / Search Facility Price Types

`GET /api/v1/facility-price-types`

Returns a paginated, filterable list of active (non-deleted) price types. All filter parameters are
optional; omitting them returns all price types. Each `LIKE`-type filter performs a case-insensitive partial
match. `Accept-Language` selects each price type's `locale` field the same way as `GET /{id}` (exact match,
falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `FacilityPriceTypeFilterRequest`'s Java field names, so
> they are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                                       | Description                                                                               |
|-----------|--------|-----------------|---------------------------------------------------|-------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                                 | Filter by code (partial, case-insensitive)                                                |
| `name`    | String | —               | —                                                 | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`    | int    | `0`             | >= 0                                              | Zero-based page index                                                                     |
| `size`    | int    | `10`            | 1 – 50                                            | Number of items per page                                                                  |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                          |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                     | Sort direction                                                                            |

> **Note:** `sort_order`, `purpose`, and `usage_example` are not filterable or sortable — only `code` and
> locale `name` are wired into the search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "FREE",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Free",
        "description": "The facility is provided to guests at no additional cost.",
        "sort_order": 1,
        "purpose": "Lets a resort advertise a facility as complimentary.",
        "usage_example": "The rooftop lounge is FREE for all guests to use."
      }
    },
    {
      "id": 2,
      "code": "INCLUDED",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Included",
        "description": "The facility's cost is already bundled into another charge (e.g. the room rate) and is not billed separately.",
        "sort_order": 2,
        "purpose": "Distinguishes a bundled-in facility from one that is free with no cost anywhere.",
        "usage_example": "Breakfast is INCLUDED in the nightly room rate."
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

## Update Facility Price Type

`PUT /api/v1/facility-price-types/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Facility Price Type Locales sub-resource endpoints below, not through this
endpoint.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Request Body

```json
{
  "sort_order": 4
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

## Delete Facility Price Type

`DELETE /api/v1/facility-price-types/{id}`

Soft-deletes the price type. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Price Type Locales

Facility Price Type Locale endpoints manage locale-specific translations for a price type. The
`{facility-price-type-id}` path parameter must reference an existing, active price type.

---

### List Facility Price Type Locales

`GET /api/v1/facility-price-types/{facility-price-type-id}/locales`

Returns a paginated list of every locale translation belonging to a price type — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET /facility-price-types/{id}`
and `GET /facility-price-types`. Optionally filtered to locales whose `code` contains a given
substring.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the parent facility price type |

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
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Fixed",
      "description": "A flat, fixed amount is charged for using the facility, per the selected price unit.",
      "sort_order": 3,
      "purpose": "Standard flat-rate billing for a facility.",
      "usage_example": "The gym costs a FIXED $10 per day."
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

### Count Facility Price Type Locales

`GET /api/v1/facility-price-types/{facility-price-type-id}/locales/count`

Returns how many active locale translations a price type currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the price type is still missing and can add a translation for
via [Create Facility Price Type Locale](#create-facility-price-type-locale) — e.g. if the
platform has `en`, `bn`, `es` and this endpoint returns `en`, `bn` for the price type, `es` is still
available; if it returns all three, every platform locale already has a translation and `POST .../locales`
for any of them will fail with `409 CONFLICT`.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the parent facility price type |

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

### Create Facility Price Type Locale

`POST /api/v1/facility-price-types/{facility-price-type-id}/locales`

Adds a new locale translation to an existing price type. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of price type and locale
must be unique — adding a locale the price type already has a translation for returns `409 CONFLICT`, backed
by a DB-level unique constraint on `(facility_price_type_id, locale_id)`. `name` must also be unique
among active translations for the same locale, regardless of which price type they belong to — reusing a
name already in use for that locale returns `409 CONFLICT`, pre-checked at the application level (no DB
constraint backs this one).

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the parent facility price type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "ফিক্সড",
  "description": "নির্বাচিত মূল্য ইউনিট অনুযায়ী সুবিধাটি ব্যবহারের জন্য একটি নির্দিষ্ট, স্থির পরিমাণ চার্জ করা হয়।",
  "sort_order": 3,
  "purpose": "একটি সুবিধার জন্য স্ট্যান্ডার্ড ফ্ল্যাট-রেট বিলিং।",
  "usage_example": "জিমের খরচ দিনে $১০ ফিক্সড।"
}
```

#### Request Fields

| Field           | Type    | Required | Validation                                                                 |
|-----------------|---------|----------|----------------------------------------------------------------------------|
| `locale_id`     | Long    | Yes      | Not null; must reference an existing locale                                |
| `name`          | String  | Yes      | Not blank, max 100 chars, unique among active translations for `locale_id` |
| `description`   | String  | Yes      | Not null                                                                   |
| `sort_order`    | Integer | Yes      | Not null                                                                   |
| `purpose`       | String  | Yes      | Not null                                                                   |
| `usage_example` | String  | Yes      | Not null                                                                   |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 6
}
```

---

### Update Facility Price Type Locale

`PUT /api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`

Updates `name`, `description`, `sort_order`, `purpose`, and `usage_example` for an existing resort facility
price type locale translation. The associated price type and locale cannot be changed after creation. `name`
is re-checked for uniqueness among active translations for the same locale, excluding this translation
itself — renaming it to a name already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the parent facility price type |
| `id`                     | Long | ID of the facility price type locale |

#### Request Body

```json
{
  "name": "ফিক্সড",
  "description": "নির্বাচিত মূল্য ইউনিট অনুযায়ী সুবিধাটি ব্যবহারের জন্য একটি নির্দিষ্ট, স্থির পরিমাণ চার্জ করা হয়।",
  "sort_order": 3,
  "purpose": "একটি সুবিধার জন্য স্ট্যান্ডার্ড ফ্ল্যাট-রেট বিলিং।",
  "usage_example": "জিমের খরচ দিনে $১০ ফিক্সড।"
}
```

#### Request Fields

| Field           | Type    | Required | Validation                                                                 |
|-----------------|---------|----------|----------------------------------------------------------------------------|
| `name`          | String  | Yes      | Not blank, max 100 chars, unique among active translations for this locale |
| `description`   | String  | Yes      | Not null                                                                   |
| `sort_order`    | Integer | Yes      | Not null                                                                   |
| `purpose`       | String  | Yes      | Not null                                                                   |
| `usage_example` | String  | Yes      | Not null                                                                   |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 6
}
```

---

### Delete Facility Price Type Locale

`DELETE /api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`

Soft-deletes a facility price type locale. The record is not removed from the database but will no
longer appear in any response.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the parent facility price type |
| `id`                     | Long | ID of the facility price type locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 6
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
  "message": "FacilityPriceType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                   |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                   |
| 404         | `ENTITY_NOT_FOUND`         | Facility price type not found, facility price type locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                                                                |
| 409         | `CONFLICT`                 | `code` already in use by another active price type (`create`); the price type already has a translation for the given `locale_id` (`create` locale); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `facility_price_type_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                                                                                       |
