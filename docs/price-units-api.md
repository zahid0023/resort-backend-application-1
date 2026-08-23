# Price Units API

Base URL: `/api/v1/price-units`

Price units represent the billing basis a resort can apply a price against (e.g. `PER_NIGHT`, `PER_DAY`,
`PER_HOUR`, `PER_PERSON`, `PER_ROOM`, `PER_BOOKING`). Each price unit is identified by a unique `code` and
must be assigned to one or more [price scopes](price-scopes-api.md) (`ROOM_CATEGORY`, `ROOM`,
`RESORT_FACILITY`) — the scopes declare *where* the price unit is allowed to be used. At least one
`price_scope_id` is required at creation time (see [Create Price Unit](#create-price-unit) below); scopes can
be added or removed afterward via the separate
[Price Unit Scope Assignments](price-unit-scope-assignments-api.md) resource. A price unit's display name,
description, and administrative guidance are locale-specific and are managed through a companion
sub-resource — Price Unit Locales — reached via `/api/v1/price-units/{price-unit-id}/locales`. All records
support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Price Unit)** and **`GET` (List/Search Price Units)** — the header's value selects
  exactly one locale translation for the price unit's `locale` field: an exact match if the price unit has
  one, otherwise `en`, otherwise `null`.
- **`GET /{price-unit-id}/locales` (List Price Unit Locales)** — the header must be present, but its value
  has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                | Description                  |
|--------|-----------------------------------------------------|------------------------------|
| POST   | `/api/v1/price-units`                               | Create a price unit          |
| GET    | `/api/v1/price-units`                               | List / search price units    |
| GET    | `/api/v1/price-units/{id}`                          | Get a price unit             |
| PUT    | `/api/v1/price-units/{id}`                          | Update a price unit          |
| DELETE | `/api/v1/price-units/{id}`                          | Delete a price unit          |
| GET    | `/api/v1/price-units/{price-unit-id}/locales`       | List a price unit's locales  |
| GET    | `/api/v1/price-units/{price-unit-id}/locales/count` | Count a price unit's locales |
| POST   | `/api/v1/price-units/{price-unit-id}/locales`       | Create a price unit locale   |
| PUT    | `/api/v1/price-units/{price-unit-id}/locales/{id}`  | Update a price unit locale   |
| DELETE | `/api/v1/price-units/{price-unit-id}/locales/{id}`  | Delete a price unit locale   |

---

## Data Model

### Price Unit

| Field          | Type    | Required | Constraints                                                           | Description                                                                                                                                    |
|----------------|---------|----------|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`           | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                      |
| `code`         | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `PER_NIGHT`, `PER_DAY`, `PER_HOUR`, `PER_PERSON`, `PER_ROOM`, `PER_BOOKING`)                                               |
| `sort_order`   | Integer | Yes      | default 0                                                             | Display order                                                                                                                                  |
| `locale`       | Object  | —        | nullable; see PriceUnitLocale below                                   | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the price unit has no translations at all) |
| `price_scopes` | Array   | —        | only present on `GET /{id}` and `GET` (list)                          | The [price scopes](price-scopes-api.md) this price unit is assigned to (e.g. `ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`)                       |

### PriceUnitLocale

| Field           | Type    | Required | Constraints                                                         | Description                                                                    |
|-----------------|---------|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                           | Auto-generated identifier                                                      |
| `locale`        | Locale  | —        | read-only, resolved from `locale_id` at creation                    | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`          | String  | Yes      | max 100 chars, unique among active translations for the same locale | Localized name of the price unit                                               |
| `description`   | String  | Yes      | not null (defaults to `""`)                                         | Localized description                                                          |
| `sort_order`    | Integer | Yes      | default 0                                                           | Display order among locale entries                                             |
| `purpose`       | String  | Yes      | not null (defaults to `""`)                                         | Localized explanation of why this price unit exists / when to use it           |
| `usage_example` | String  | Yes      | not null (defaults to `""`)                                         | Localized example scenario shown to administrators                             |

---

## Create Price Unit

`POST /api/v1/price-units`

Creates a new price unit together with the [price scopes](price-scopes-api.md) it may be used in and exactly
**one** initial locale translation. `code` must be unique among active, non-deleted price units —
attempting to reuse an existing code returns `409 CONFLICT`.

**At least one `price_scope_id` is required** — it declares which scopes (`ROOM_CATEGORY`, `ROOM`,
`RESORT_FACILITY`, etc.) the price unit is allowed to be applied to. Every id must reference an existing,
active price scope — an unknown id returns `404 ENTITY_NOT_FOUND`. Additional scopes can be assigned or
removed afterward via the [Price Unit Scope Assignments](price-unit-scope-assignments-api.md) resource.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Price Unit Locales sub-resource below.

### Request Body

```json
{
  "code": "PER_NIGHT",
  "sort_order": 1,
  "price_scope_ids": [
    1,
    2
  ],
  "locale": {
    "name": "Per Night",
    "description": "Price applied once for each night of the stay.",
    "sort_order": 1,
    "purpose": "Standard nightly rate used for most room bookings.",
    "usage_example": "A deluxe room costs $120 per night, so a 3-night stay totals $360."
  }
}
```

### Request Fields

| Field             | Type       | Required | Validation                                                                                 |
|-------------------|------------|----------|--------------------------------------------------------------------------------------------|
| `code`            | String     | Yes      | Not blank, max 50 chars, unique among active records                                       |
| `sort_order`      | Integer    | Yes      | Not null                                                                                   |
| `price_scope_ids` | Long array | Yes      | Not empty; every id must reference an existing, active [price scope](price-scopes-api.md)  |
| `locale`          | Object     | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

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

## Get Price Unit

`GET /api/v1/price-units/{id}`

Returns a single active price unit by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the price unit has no translations at all). To
fetch every translation a price unit has, use [List Price Unit Locales](#list-price-unit-locales) below.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price unit |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "PER_NIGHT",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Per Night",
      "description": "Price applied once for each night of the stay.",
      "sort_order": 1,
      "purpose": "Standard nightly rate used for most room bookings.",
      "usage_example": "A deluxe room costs $120 per night, so a 3-night stay totals $360."
    },
    "price_scopes": [
      {
        "id": 1,
        "code": "ROOM_CATEGORY",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Room Category",
          "description": "Applies to room category pricing.",
          "sort_order": 1
        }
      }
    ]
  }
}
```

---

## List / Search Price Units

`GET /api/v1/price-units`

Returns a paginated, filterable list of active (non-deleted) price units. All filter parameters are
optional; omitting them returns all price units. Each `LIKE`-type filter performs a case-insensitive partial
match. `Accept-Language` selects each price unit's `locale` field the same way as `GET /{id}` (exact match,
falls back to `en`, then `null`).

To fetch every price unit usable in a given context (e.g. every price unit a [Resort Facility
Price](resort-facility-prices-api.md) may reference), filter by `priceScopeCodes`:
`?priceScopeCodes=RESORT_FACILITY`. `priceScopeCodes` matches against `price_scopes.code` directly (e.g.
`ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`) so the frontend doesn't need to resolve a scope id first; a price
unit with *any* active assignment to one of the given codes matches (multiple codes are OR'd together).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `PriceUnitFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter          | Type     | Default         | Constraints                                       | Description                                                                                    |
|---------------------|----------|-----------------|---------------------------------------------------|--------------------------------------------------------------------------------------------------|
| `code`              | String   | —               | —                                                 | Filter by code (partial, case-insensitive)                                                       |
| `name`              | String   | —               | —                                                 | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale        |
| `priceScopeCodes`   | String[] | —               | must match an existing `price_scopes.code`        | Filter to price units assigned to any of the given price scope codes (e.g. `RESORT_FACILITY`)    |
| `page`              | int      | `0`             | >= 0                                              | Zero-based page index                                                                             |
| `size`              | int      | `10`            | 1 – 50                                            | Number of items per page                                                                          |
| `sortBy`            | String   | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                                  |
| `sortDir`           | String   | `ASC`           | `ASC`, `DESC`                                     | Sort direction                                                                                    |

> **Note:** `sort_order`, `purpose`, and `usage_example` are not filterable or sortable — only `code` and
> locale `name` are wired into the search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Per Night",
        "description": "Price applied once for each night of the stay.",
        "sort_order": 1,
        "purpose": "Standard nightly rate used for most room bookings.",
        "usage_example": "A deluxe room costs $120 per night, so a 3-night stay totals $360."
      },
      "price_scopes": [
        {
          "id": 1,
          "code": "ROOM_CATEGORY",
          "sort_order": 1
        },
        {
          "id": 2,
          "code": "ROOM",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 2,
      "code": "PER_DAY",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Per Day",
        "description": "Price applied once for each calendar day, regardless of overnight stay.",
        "sort_order": 2,
        "purpose": "Used for day-use bookings or facilities billed by the day rather than the night.",
        "usage_example": "A meeting room costs $200 per day for a full-day conference booking."
      },
      "price_scopes": [
        {
          "id": 3,
          "code": "RESORT_FACILITY",
          "sort_order": 3
        }
      ]
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

## Update Price Unit

`PUT /api/v1/price-units/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Price Unit Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price unit |

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

## Delete Price Unit

`DELETE /api/v1/price-units/{id}`

Soft-deletes the price unit. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price unit |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Price Unit Locales

Price Unit Locale endpoints manage locale-specific translations for a price unit. The `{price-unit-id}` path
parameter must reference an existing, active price unit.

---

### List Price Unit Locales

`GET /api/v1/price-units/{price-unit-id}/locales`

Returns a paginated list of every locale translation belonging to a price unit — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET /price-units/{id}` and
`GET /price-units`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-unit-id` | Long | ID of the parent price unit |

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
      "name": "Per Night",
      "description": "Price applied once for each night of the stay.",
      "sort_order": 1,
      "purpose": "Standard nightly rate used for most room bookings.",
      "usage_example": "A deluxe room costs $120 per night, so a 3-night stay totals $360."
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

### Count Price Unit Locales

`GET /api/v1/price-units/{price-unit-id}/locales/count`

Returns how many active locale translations a price unit currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the price unit is still missing and can add a translation for
via [Create Price Unit Locale](#create-price-unit-locale) — e.g. if the platform has `en`, `bn`, `es` and
this endpoint returns `en`, `bn` for the price unit, `es` is still available; if it returns all three,
every platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-unit-id` | Long | ID of the parent price unit |

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

### Create Price Unit Locale

`POST /api/v1/price-units/{price-unit-id}/locales`

Adds a new locale translation to an existing price unit. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of price unit and locale
must be unique — adding a locale the price unit already has a translation for returns `409 CONFLICT`, backed
by a DB-level unique constraint on `(price_unit_id, locale_id)`. `name` must also be unique among active
translations for the same locale, regardless of which price unit they belong to — reusing a name already in
use for that locale returns `409 CONFLICT`, pre-checked at the application level (no DB constraint backs
this one).

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-unit-id` | Long | ID of the parent price unit |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "প্রতি রাত",
  "description": "থাকার প্রতিটি রাতের জন্য একবার প্রযোজ্য মূল্য।",
  "sort_order": 1,
  "purpose": "বেশিরভাগ রুম বুকিংয়ের জন্য ব্যবহৃত আদর্শ রাত্রিকালীন হার।",
  "usage_example": "একটি ডিলাক্স রুমের ভাড়া প্রতি রাতে $১২০, তাই ৩ রাতের থাকার মোট খরচ $৩৬০।"
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
  "id": 2
}
```

---

### Update Price Unit Locale

`PUT /api/v1/price-units/{price-unit-id}/locales/{id}`

Updates `name`, `description`, `sort_order`, `purpose`, and `usage_example` for an existing price unit
locale translation. The associated price unit and locale cannot be changed after creation. `name` is
re-checked for uniqueness among active translations for the same locale, excluding this translation itself
— renaming it to a name already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-unit-id` | Long | ID of the parent price unit |
| `id`            | Long | ID of the price unit locale |

#### Request Body

```json
{
  "name": "প্রতি রাত",
  "description": "থাকার প্রতিটি রাতের জন্য একবার প্রযোজ্য মূল্য।",
  "sort_order": 1,
  "purpose": "বেশিরভাগ রুম বুকিংয়ের জন্য ব্যবহৃত আদর্শ রাত্রিকালীন হার।",
  "usage_example": "একটি ডিলাক্স রুমের ভাড়া প্রতি রাতে $১২০, তাই ৩ রাতের থাকার মোট খরচ $৩৬০।"
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
  "id": 2
}
```

---

### Delete Price Unit Locale

`DELETE /api/v1/price-units/{price-unit-id}/locales/{id}`

Soft-deletes a price unit locale. The record is not removed from the database but will no longer appear in
any response.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-unit-id` | Long | ID of the parent price unit |
| `id`            | Long | ID of the price unit locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
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
  "message": "PriceUnit not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                   |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an empty `price_scope_ids` array; or an unsupported `sortBy` query value                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Price unit not found, price unit locale not found, one of the `price_scope_ids` not found (create), or the locale referenced by `locale_id` not found (locale creation)                                                                                                 |
| 409         | `CONFLICT`                 | `code` already in use by another active price unit (`create`); the price unit already has a translation for the given `locale_id` (`create` locale); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `price_unit_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                                                                                                |
