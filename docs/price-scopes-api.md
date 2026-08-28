# Price Scopes API

Base URL: `/api/v1/price-scopes`

Price scopes define where pricing can apply (e.g. `ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`) — for
example, a per-day price unit applies at the room category or room scope, while a per-hour price unit
applies at the resort facility scope. [Price Unit Scope Assignments](price-unit-scope-assignments-api.md)
reference these price scope records. Each scope is identified by a unique `code`. A price scope's display
name and description are
locale-specific and are managed through a companion sub-resource — Price Scope Locales — reached via
`/api/v1/price-scopes/{price-scope-id}/locales`. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Price Scope)** and **`GET` (List/Search Price Scopes)** — the header's value
  selects exactly one locale translation for the price scope's `locale` field: an exact match if the
  price scope has one, otherwise `en`, otherwise `null`.
- **`GET /{price-scope-id}/locales` (List Price Scope Locales)** — the header must be present, but
  its value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`),
  not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                  | Description                   |
|--------|-------------------------------------------------------|-------------------------------|
| POST   | `/api/v1/price-scopes`                                | Create a price scope          |
| GET    | `/api/v1/price-scopes`                                | List / search price scopes    |
| GET    | `/api/v1/price-scopes/count`                          | Count active price scopes     |
| GET    | `/api/v1/price-scopes/{id}`                           | Get a price scope             |
| PUT    | `/api/v1/price-scopes/{id}`                           | Update a price scope          |
| DELETE | `/api/v1/price-scopes/{id}`                           | Delete a price scope          |
| GET    | `/api/v1/price-scopes/{price-scope-id}/locales`       | List a price scope's locales  |
| GET    | `/api/v1/price-scopes/{price-scope-id}/locales/count` | Count a price scope's locales |
| POST   | `/api/v1/price-scopes/{price-scope-id}/locales`       | Create a price scope locale   |
| PUT    | `/api/v1/price-scopes/{price-scope-id}/locales/{id}`  | Update a price scope locale   |
| DELETE | `/api/v1/price-scopes/{price-scope-id}/locales/{id}`  | Delete a price scope locale   |

---

## Data Model

### Price Scope

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                     |
|--------------|---------|----------|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                       |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`)                                                                                 |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                                   |
| `locale`     | Object  | —        | nullable; see PriceScopeLocale below                                  | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the price scope has no translations at all) |

### PriceScopeLocale

| Field         | Type    | Required | Constraints                                                         | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                           | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation                    | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 100 chars, unique among active translations for the same locale | Localized name of the scope                                                    |
| `description` | String  | Yes      | not null (defaults to `""`)                                         | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                                           | Display order among locale entries                                             |

---

## Create Price Scope

`POST /api/v1/price-scopes`

Creates a new price scope together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted price scopes — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Price Scope Locales sub-resource below.

### Request Body

```json
{
  "code": "ROOM_CATEGORY",
  "sort_order": 1,
  "locale": {
    "name": "Room Category",
    "description": "",
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

| Field         | Type    | Required | Validation                                                          |
|---------------|---------|----------|---------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for `en` |
| `description` | String  | Yes      | Not null                                                            |
| `sort_order`  | Integer | Yes      | Not null                                                            |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Price Scope

`GET /api/v1/price-scopes/{id}`

Returns a single active price scope by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the price scope has no translations at
all). To fetch every translation a price scope has, use
[List Price Scope Locales](#list-price-scope-locales) below.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the price scope |

### Response `200 OK`

```json
{
  "data": {
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
      "description": "",
      "sort_order": 1
    }
  }
}
```

---

## Count Active Price Scopes

`GET /api/v1/price-scopes/count`

Returns how many active, non-deleted price scopes exist, together with each one's `code`. `count` is
always `codes.length` — both come from the same query, so there's no separate tally to drift out of sync
with the list.

### Response `200 OK`

```json
{
  "count": 2,
  "codes": [
    "ROOM_CATEGORY",
    "ROOM"
  ]
}
```

---

## List / Search Price Scopes

`GET /api/v1/price-scopes`

Returns a paginated, filterable list of active (non-deleted) price scopes. All filter parameters are
optional; omitting them returns all price scopes. Each `LIKE`-type filter performs a case-insensitive
partial match. `Accept-Language` selects each price scope's `locale` field the same way as `GET /{id}`
(exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `PriceScopeFilterRequest`'s Java field names, so they
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
        "description": "",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "ROOM",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Room",
        "description": "",
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

## Update Price Scope

`PUT /api/v1/price-scopes/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Price Scope Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the price scope |

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

## Delete Price Scope

`DELETE /api/v1/price-scopes/{id}`

Soft-deletes the price scope. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the price scope |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Price Scope Locales

Price Scope Locale endpoints manage locale-specific name/description translations for a price scope.
The `{price-scope-id}` path parameter must reference an existing, active price scope.

---

### List Price Scope Locales

`GET /api/v1/price-scopes/{price-scope-id}/locales`

Returns a paginated list of every locale translation belonging to a price scope — this is the only way
to see more than the single Accept-Language-matched translation returned by
`GET /price-scopes/{id}` and `GET /price-scopes`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `price-scope-id` | Long | ID of the parent price scope |

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
      "name": "Room Category",
      "description": "",
      "sort_order": 1
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

### Count Price Scope Locales

`GET /api/v1/price-scopes/{price-scope-id}/locales/count`

Returns how many active locale translations a price scope currently has, plus the `code` of each
one. Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the price scope is still missing and can add a
translation for via [Create Price Scope Locale](#create-price-scope-locale) — e.g. if the
platform has `en`, `bn`, `es` and this endpoint returns `en`, `bn` for the price scope, `es` is still
available; if it returns all three, every platform locale already has a translation and
`POST .../locales` for any of them will fail with `409 CONFLICT`.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `price-scope-id` | Long | ID of the parent price scope |

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

### Create Price Scope Locale

`POST /api/v1/price-scopes/{price-scope-id}/locales`

Adds a new locale translation to an existing price scope. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of price scope
and locale must be unique — adding a locale the price scope already has a translation for returns
`409 CONFLICT`, backed by a DB-level unique constraint on `(price_scope_id, locale_id)`. `name` must
also be unique among active translations for the same locale, regardless of which price scope they
belong to — reusing a name already in use for that locale returns `409 CONFLICT`, pre-checked at the
application level (no DB constraint backs this one).

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `price-scope-id` | Long | ID of the parent price scope |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রুম ক্যাটাগরি",
  "description": "",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                 |
|---------------|---------|----------|----------------------------------------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale                                |
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for `locale_id` |
| `description` | String  | Yes      | Not null                                                                   |
| `sort_order`  | Integer | Yes      | Not null                                                                   |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 4
}
```

---

### Update Price Scope Locale

`PUT /api/v1/price-scopes/{price-scope-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing price scope locale translation. The
associated price scope and locale cannot be changed after creation. `name` is re-checked for
uniqueness among active translations for the same locale, excluding this translation itself — renaming it
to a name already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `price-scope-id` | Long | ID of the parent price scope |
| `id`             | Long | ID of the price scope locale |

#### Request Body

```json
{
  "name": "রুম ক্যাটাগরি",
  "description": "",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                 |
|---------------|---------|----------|----------------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for this locale |
| `description` | String  | Yes      | Not null                                                                   |
| `sort_order`  | Integer | Yes      | Not null                                                                   |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

### Delete Price Scope Locale

`DELETE /api/v1/price-scopes/{price-scope-id}/locales/{id}`

Soft-deletes a price scope locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `price-scope-id` | Long | ID of the parent price scope |
| `id`             | Long | ID of the price scope locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
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
  "message": "PriceScope not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                     |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                     |
| 404         | `ENTITY_NOT_FOUND`         | Price scope not found, price scope locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                                                                                  |
| 409         | `CONFLICT`                 | `code` already in use by another active price scope (`create`); the price scope already has a translation for the given `locale_id` (`create` locale); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `price_scope_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                                                                                                 |
