# Price Types API

Base URL: `/api/v1/price-types`

Price types represent the pricing rules a resort can apply (e.g. `BAS` for base pricing, `WKD`/`WKE` for
weekday/weekend pricing, `HOL` for holiday pricing, `SPECIAL` for promotions). Each price type is identified
by a unique `code` and must be assigned to one or more [price scopes](price-scopes-api.md) (`ROOM_CATEGORY`,
`ROOM`, `RESORT_FACILITY`) — the scopes declare *where* the price type is allowed to be used. At least one
`price_scope_id` is required at creation time (see [Create Price Type](#create-price-type) below); scopes can
be added or removed afterward via the separate
[Price Type Scope Assignments](price-type-scope-assignments-api.md) resource. A price type's display name,
description, and administrative guidance are locale-specific and are managed through a companion
sub-resource — Price Type Locales — reached via `/api/v1/price-types/{price-type-id}/locales`. All records
support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Price Type)** and **`GET` (List/Search Price Types)** — the header's value selects
  exactly one locale translation for the price type's `locale` field: an exact match if the price type has
  one, otherwise `en`, otherwise `null`.
- **`GET /{price-type-id}/locales` (List Price Type Locales)** — the header must be present, but its value
  has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                | Description                  |
|--------|-----------------------------------------------------|------------------------------|
| POST   | `/api/v1/price-types`                               | Create a price type          |
| GET    | `/api/v1/price-types`                               | List / search price types    |
| GET    | `/api/v1/price-types/{id}`                          | Get a price type             |
| PUT    | `/api/v1/price-types/{id}`                          | Update a price type          |
| DELETE | `/api/v1/price-types/{id}`                          | Delete a price type          |
| GET    | `/api/v1/price-types/{price-type-id}/locales`       | List a price type's locales  |
| GET    | `/api/v1/price-types/{price-type-id}/locales/count` | Count a price type's locales |
| POST   | `/api/v1/price-types/{price-type-id}/locales`       | Create a price type locale   |
| PUT    | `/api/v1/price-types/{price-type-id}/locales/{id}`  | Update a price type locale   |
| DELETE | `/api/v1/price-types/{price-type-id}/locales/{id}`  | Delete a price type locale   |

---

## Data Model

### Price Type

| Field          | Type    | Required | Constraints                                                           | Description                                                                                                                                    |
|----------------|---------|----------|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`           | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                      |
| `code`         | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `BAS`, `WKD`, `WKE`, `HOL`, `SPECIAL`)                                                                                     |
| `sort_order`   | Integer | Yes      | default 0                                                             | Display order                                                                                                                                  |
| `locale`       | Object  | —        | nullable; see PriceTypeLocale below                                   | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the price type has no translations at all) |
| `price_scopes` | Array   | —        | only present on `GET /{id}` and `GET` (list)                          | The [price scopes](price-scopes-api.md) this price type is assigned to (e.g. `ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`)                       |

### PriceTypeLocale

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

## Create Price Type

`POST /api/v1/price-types`

Creates a new price type together with the [price scopes](price-scopes-api.md) it may be used in and exactly
**one** initial locale translation. `code` must be unique among active, non-deleted price types — attempting
to reuse an existing code returns `409 CONFLICT`.

**At least one `price_scope_id` is required** — it declares which scopes (`ROOM_CATEGORY`, `ROOM`,
`RESORT_FACILITY`, etc.) the price type is allowed to be applied to. Every id must reference an existing,
active price scope — an unknown id returns `404 ENTITY_NOT_FOUND`. Additional scopes can be assigned or
removed afterward via the [Price Type Scope Assignments](price-type-scope-assignments-api.md) resource.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Price Type Locales sub-resource below.

### Request Body

```json
{
  "code": "WKE",
  "sort_order": 3,
  "price_scope_ids": [
    1,
    2
  ],
  "locale": {
    "name": "Weekend Price",
    "description": "Price applied to bookings made on Saturdays and Sundays.",
    "sort_order": 3,
    "purpose": "Allows higher pricing during peak weekend demand.",
    "usage_example": "Football Ground A costs $50/hour on weekdays and $70/hour on weekends."
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

## Get Price Type

`GET /api/v1/price-types/{id}`

Returns a single active price type by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the price type has no translations at all). To
fetch every translation a price type has, use [List Price Type Locales](#list-price-type-locales) below.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price type |

### Response `200 OK`

```json
{
  "data": {
    "id": 3,
    "code": "WKE",
    "sort_order": 3,
    "locale": {
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Weekend Price",
      "description": "Price applied to bookings made on Saturdays and Sundays.",
      "sort_order": 3,
      "purpose": "Allows higher pricing during peak weekend demand.",
      "usage_example": "Football Ground A costs $50/hour on weekdays and $70/hour on weekends."
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

## List / Search Price Types

`GET /api/v1/price-types`

Returns a paginated, filterable list of active (non-deleted) price types. All filter parameters are
optional; omitting them returns all price types. Each `LIKE`-type filter performs a case-insensitive partial
match. `Accept-Language` selects each price type's `locale` field the same way as `GET /{id}` (exact match,
falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `PriceTypeFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

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
      "code": "BAS",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Base Price",
        "description": "Standard price applied by default when no other price type matches.",
        "sort_order": 1,
        "purpose": "Serves as the fallback pricing rule for all bookings.",
        "usage_example": "A standard room costs $100/night under the base price."
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
        },
        {
          "id": 3,
          "code": "RESORT_FACILITY",
          "sort_order": 3
        }
      ]
    },
    {
      "id": 2,
      "code": "WKD",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Weekday Price",
        "description": "Price applied to bookings made on weekdays (Monday through Friday).",
        "sort_order": 2,
        "purpose": "Allows lower pricing during off-peak weekday periods.",
        "usage_example": "A room costs $90/night on weekdays compared to $130/night on weekends."
      },
      "price_scopes": [
        {
          "id": 1,
          "code": "ROOM_CATEGORY",
          "sort_order": 1
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

## Update Price Type

`PUT /api/v1/price-types/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Price Type Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price type |

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

## Delete Price Type

`DELETE /api/v1/price-types/{id}`

Soft-deletes the price type. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the price type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Price Type Locales

Price Type Locale endpoints manage locale-specific translations for a price type. The `{price-type-id}` path
parameter must reference an existing, active price type.

---

### List Price Type Locales

`GET /api/v1/price-types/{price-type-id}/locales`

Returns a paginated list of every locale translation belonging to a price type — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET /price-types/{id}` and
`GET /price-types`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-type-id` | Long | ID of the parent price type |

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
      "name": "Weekend Price",
      "description": "Price applied to bookings made on Saturdays and Sundays.",
      "sort_order": 3,
      "purpose": "Allows higher pricing during peak weekend demand.",
      "usage_example": "Football Ground A costs $50/hour on weekdays and $70/hour on weekends."
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

### Count Price Type Locales

`GET /api/v1/price-types/{price-type-id}/locales/count`

Returns how many active locale translations a price type currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the price type is still missing and can add a translation for
via [Create Price Type Locale](#create-price-type-locale) — e.g. if the platform has `en`, `bn`, `es` and
this endpoint returns `en`, `bn` for the price type, `es` is still available; if it returns all three,
every platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-type-id` | Long | ID of the parent price type |

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

### Create Price Type Locale

`POST /api/v1/price-types/{price-type-id}/locales`

Adds a new locale translation to an existing price type. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of price type and locale
must be unique — adding a locale the price type already has a translation for returns `409 CONFLICT`, backed
by a DB-level unique constraint on `(price_type_id, locale_id)`. `name` must also be unique among active
translations for the same locale, regardless of which price type they belong to — reusing a name already in
use for that locale returns `409 CONFLICT`, pre-checked at the application level (no DB constraint backs
this one).

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-type-id` | Long | ID of the parent price type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "সপ্তাহান্তের মূল্য",
  "description": "শনিবার এবং রবিবার করা বুকিংয়ের জন্য প্রযোজ্য মূল্য।",
  "sort_order": 3,
  "purpose": "সর্বোচ্চ চাহিদার সময় সপ্তাহান্তে উচ্চ মূল্য নির্ধারণের সুযোগ দেয়।",
  "usage_example": "ফুটবল গ্রাউন্ড এ সপ্তাহের দিনগুলোতে $৫০/ঘণ্টা এবং সপ্তাহান্তে $৭০/ঘণ্টা।"
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

### Update Price Type Locale

`PUT /api/v1/price-types/{price-type-id}/locales/{id}`

Updates `name`, `description`, `sort_order`, `purpose`, and `usage_example` for an existing price type
locale translation. The associated price type and locale cannot be changed after creation. `name` is
re-checked for uniqueness among active translations for the same locale, excluding this translation itself
— renaming it to a name already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-type-id` | Long | ID of the parent price type |
| `id`            | Long | ID of the price type locale |

#### Request Body

```json
{
  "name": "সপ্তাহান্তের মূল্য",
  "description": "শনিবার এবং রবিবার করা বুকিংয়ের জন্য প্রযোজ্য মূল্য।",
  "sort_order": 3,
  "purpose": "সর্বোচ্চ চাহিদার সময় সপ্তাহান্তে উচ্চ মূল্য নির্ধারণের সুযোগ দেয়।",
  "usage_example": "ফুটবল গ্রাউন্ড এ সপ্তাহের দিনগুলোতে $৫০/ঘণ্টা এবং সপ্তাহান্তে $৭০/ঘণ্টা।"
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

### Delete Price Type Locale

`DELETE /api/v1/price-types/{price-type-id}/locales/{id}`

Soft-deletes a price type locale. The record is not removed from the database but will no longer appear in
any response.

#### Path Parameters

| Parameter       | Type | Description                 |
|-----------------|------|-----------------------------|
| `price-type-id` | Long | ID of the parent price type |
| `id`            | Long | ID of the price type locale |

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
  "message": "PriceType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                   |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an empty `price_scope_ids` array; or an unsupported `sortBy` query value                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Price type not found, price type locale not found, one of the `price_scope_ids` not found (create), or the locale referenced by `locale_id` not found (locale creation)                                                                                                 |
| 409         | `CONFLICT`                 | `code` already in use by another active price type (`create`); the price type already has a translation for the given `locale_id` (`create` locale); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `price_type_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                                                                                                |
