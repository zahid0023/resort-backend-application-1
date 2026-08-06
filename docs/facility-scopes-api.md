# Facility Scopes API

Base URL: `/api/v1/facility-scopes`

Facility scopes define where a facility can be applied (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`), each
identified by a unique `code`. A facility scope's display name and description are locale-specific and are
managed through a companion sub-resource — Facility Scope Locales — reached via
`/api/v1/facility-scopes/{facility-scope-id}/locales`. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Facility Scope)** and **`GET` (List/Search Facility Scopes)** — the header's value
  selects exactly one locale translation for the facility scope's `locale` field: an exact match if the
  facility scope has one, otherwise `en`, otherwise `null`.
- **`GET /{facility-scope-id}/locales` (List Facility Scope Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                        | Description                      |
|--------|-------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/facility-scopes`                                   | Create a facility scope          |
| GET    | `/api/v1/facility-scopes`                                   | List / search facility scopes    |
| GET    | `/api/v1/facility-scopes/count`                             | Count active facility scopes     |
| GET    | `/api/v1/facility-scopes/{id}`                              | Get a facility scope             |
| PUT    | `/api/v1/facility-scopes/{id}`                              | Update a facility scope          |
| DELETE | `/api/v1/facility-scopes/{id}`                              | Delete a facility scope          |
| GET    | `/api/v1/facility-scopes/{facility-scope-id}/locales`       | List a facility scope's locales  |
| GET    | `/api/v1/facility-scopes/{facility-scope-id}/locales/count` | Count a facility scope's locales |
| POST   | `/api/v1/facility-scopes/{facility-scope-id}/locales`       | Create a facility scope locale   |
| PUT    | `/api/v1/facility-scopes/{facility-scope-id}/locales/{id}`  | Update a facility scope locale   |
| DELETE | `/api/v1/facility-scopes/{facility-scope-id}/locales/{id}`  | Delete a facility scope locale   |

---

## Data Model

### Facility Scope

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                        |
|--------------|---------|----------|-----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                          |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`)                                                                                             |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                                      |
| `locale`     | Object  | —        | nullable; see FacilityScopeLocale below                               | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the facility scope has no translations at all) |

### FacilityScopeLocale

| Field         | Type    | Required | Constraints                                                         | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                           | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation                    | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 100 chars, unique among active translations for the same locale | Localized name of the scope                                                    |
| `description` | String  | Yes      | not null (defaults to `""`)                                         | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                                           | Display order among locale entries                                             |

---

## Create Facility Scope

`POST /api/v1/facility-scopes`

Creates a new facility scope together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted facility scopes — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Facility Scope Locales sub-resource below.

### Request Body

```json
{
  "code": "RESORT",
  "sort_order": 1,
  "locale": {
    "name": "Resort",
    "description": "Resort-wide facility",
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

## Get Facility Scope

`GET /api/v1/facility-scopes/{id}`

Returns a single active facility scope by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the facility scope has no translations at all).
To fetch every translation a facility scope has, use
[List Facility Scope Locales](#list-facility-scope-locales) below.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility scope |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "RESORT",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Resort",
      "description": "Resort-wide facility",
      "sort_order": 1
    }
  }
}
```

---

## Count Active Facility Scopes

`GET /api/v1/facility-scopes/count`

Returns how many active, non-deleted facility scopes exist, together with each one's `code`. `count` is
always `codes.length` — both come from the same query, so there's no separate tally to drift out of sync
with the list.

### Response `200 OK`

```json
{
  "count": 2,
  "codes": [
    "RESORT",
    "ROOM_CATEGORY"
  ]
}
```

---

## List / Search Facility Scopes

`GET /api/v1/facility-scopes`

Returns a paginated, filterable list of active (non-deleted) facility scopes. All filter parameters are
optional; omitting them returns all facility scopes. Each `LIKE`-type filter performs a case-insensitive
partial match. `Accept-Language` selects each facility scope's `locale` field the same way as `GET /{id}`
(exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `FacilityScopeFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                                            | Description                                                                               |
|-----------|--------|-----------------|--------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                                      | Filter by code (partial, case-insensitive)                                                |
| `name`    | String | —               | —                                                      | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`    | int    | `0`             | >= 0                                                   | Zero-based page index                                                                     |
| `size`    | int    | `10`            | 1 – 50                                                 | Number of items per page                                                                  |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `sortOrder` (`id` NOT selectable) | Field to sort by                                                                          |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                          | Sort direction                                                                            |

> **Note:** `sort_order` is not filterable — the existing search-field infrastructure only supports String
> fields, and adding an exact-match Integer filter was intentionally left out of this endpoint. `sort_order`
> remains available for creation, update, and sorting.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "RESORT",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Resort",
        "description": "Resort-wide facility",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "ROOM_CATEGORY",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Room Category",
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
    "sortOrder"
  ],
  "searchable_fields": [
    "code",
    "name"
  ]
}
```

---

## Update Facility Scope

`PUT /api/v1/facility-scopes/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Facility Scope Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility scope |

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

## Delete Facility Scope

`DELETE /api/v1/facility-scopes/{id}`

Soft-deletes the facility scope. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility scope |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Scope Locales

Facility Scope Locale endpoints manage locale-specific name/description translations for a facility scope.
The `{facility-scope-id}` path parameter must reference an existing, active facility scope.

---

### List Facility Scope Locales

`GET /api/v1/facility-scopes/{facility-scope-id}/locales`

Returns a paginated list of every locale translation belonging to a facility scope — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /facility-scopes/{id}` and
`GET /facility-scopes`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the parent facility scope |

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
      "name": "Resort",
      "description": "Resort-wide facility",
      "sort_order": 1
    },
    {
      "id": 4,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "রিসোর্ট",
      "description": "রিসোর্টের সুযোগ-সুবিধা",
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

### Count Facility Scope Locales

`GET /api/v1/facility-scopes/{facility-scope-id}/locales/count`

Returns how many active locale translations a facility scope currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the facility scope is still missing and can add a translation
for via [Create Facility Scope Locale](#create-facility-scope-locale) — e.g. if the platform has `en`,
`bn`, `es` and this endpoint returns `en`, `bn` for the facility scope, `es` is still available; if it
returns all three, every platform locale already has a translation and `POST .../locales` for any of them
will fail with `409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the parent facility scope |

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

### Create Facility Scope Locale

`POST /api/v1/facility-scopes/{facility-scope-id}/locales`

Adds a new locale translation to an existing facility scope. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility scope and locale
must be unique — adding a locale the facility scope already has a translation for returns `409 CONFLICT`.
`name` must also be unique among active translations for the same locale, regardless of which facility scope
they belong to — reusing a name already in use for that locale returns `409 CONFLICT`. Both checks are
pre-checked at the application level before any write (the facility-scope/locale combination is additionally
backed by a DB-level unique constraint on `(facility_scope_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the parent facility scope |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রিসোর্ট",
  "description": "রিসোর্টের সুযোগ-সুবিধা",
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

### Update Facility Scope Locale

`PUT /api/v1/facility-scopes/{facility-scope-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing facility scope locale translation. The
associated facility scope and locale cannot be changed after creation. `name` is re-checked for uniqueness
among active translations for the same locale, excluding this translation itself — renaming it to a name
already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the parent facility scope |
| `id`                | Long | ID of the facility scope locale |

#### Request Body

```json
{
  "name": "রিসোর্ট",
  "description": "রিসোর্টের সুযোগ-সুবিধা",
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

### Delete Facility Scope Locale

`DELETE /api/v1/facility-scopes/{facility-scope-id}/locales/{id}`

Soft-deletes a facility scope locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the parent facility scope |
| `id`                | Long | ID of the facility scope locale |

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
  "message": "FacilityScope not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                        |
|-------------|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                                        |
| 404         | `ENTITY_NOT_FOUND`         | Facility scope not found, facility scope locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                                                                                               |
| 409         | `CONFLICT`                 | `code` already in use by another active facility scope (`create`); the facility scope already has a translation for the given `locale_id` (`create` locale, pre-checked); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `facility_scope_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                                                                                                                 |
