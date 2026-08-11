# Facility Groups API

Base URL: `/api/v1/facility-groups`

Facility groups categorize facilities (e.g. `DINING`, `WELLNESS`, `RECREATION`, `ACCOMMODATION`) and carry the
icon used to render them in the UI. A facility group must belong to at least one facility scope (e.g.
`RESORT`, `ROOM_CATEGORY`, `ROOM`) at creation time; additional scopes afterward are managed via the
[Facility Group Scope Assignments API](facility-group-scope-assignments-api.md) under the facility scope
resource, not through this API. A facility group's display name and description are locale-specific and are
managed through a companion sub-resource — Facility Group Locales — reached via
`/api/v1/facility-groups/{facility-group-id}/locales`. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Facility Group)** and **`GET` (List/Search Facility Groups)** — the header's value
  selects exactly one locale translation for the facility group's `locale` field: an exact match if the
  facility group has one, otherwise `en`, otherwise `null`.
- **`GET /{facility-group-id}/locales` (List Facility Group Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                        | Description                      |
|--------|-------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/facility-groups`                                   | Create a facility group          |
| GET    | `/api/v1/facility-groups`                                   | List / search facility groups    |
| GET    | `/api/v1/facility-groups/{id}`                              | Get a facility group             |
| PUT    | `/api/v1/facility-groups/{id}`                              | Update a facility group          |
| DELETE | `/api/v1/facility-groups/{id}`                              | Delete a facility group          |
| GET    | `/api/v1/facility-groups/{facility-group-id}/locales`       | List a facility group's locales  |
| GET    | `/api/v1/facility-groups/{facility-group-id}/locales/count` | Count a facility group's locales |
| POST   | `/api/v1/facility-groups/{facility-group-id}/locales`       | Create a facility group locale   |
| PUT    | `/api/v1/facility-groups/{facility-group-id}/locales/{id}`  | Update a facility group locale   |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/locales/{id}`  | Delete a facility group locale   |

---

## Data Model

### Facility Group

| Field             | Type    | Required | Constraints                                                                                                | Description                                                                                                                                                                     |
|-------------------|---------|----------|------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`              | Long    | —        | read-only                                                                                                  | Auto-generated identifier                                                                                                                                                       |
| `code`            | String  | Yes      | max 100 chars, unique among active records (app-level only — no DB constraint); set at creation, immutable | Internal code (e.g. `DINING`, `WELLNESS`, `RECREATION`, `ACCOMMODATION`)                                                                                                        |
| `sort_order`      | Integer | Yes      | default 1                                                                                                  | Display order                                                                                                                                                                   |
| `icon_type`       | String  | Yes      | max 100 chars                                                                                              | Icon library/source (e.g. `LUCIDE`)                                                                                                                                             |
| `icon_value`      | String  | No       | nullable                                                                                                   | Icon name/path within `icon_type`'s library (e.g. `UtensilsCrossed`)                                                                                                            |
| `icon_meta`       | Object  | No       | nullable, free-form JSON                                                                                   | Icon rendering metadata (e.g. `{"size": 24, "color": "#f59e0b", "stroke_width": 1.5}`)                                                                                          |
| `locale`          | Object  | —        | nullable; see FacilityGroupLocale below                                                                    | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the facility group has no translations at all)                              |
| `facility_scopes` | Array   | —        | read-only; see Facility Scope in [Facility Scopes API](facility-scopes-api.md)                             | The facility scopes currently assigned to this group, managed via `POST/DELETE /api/v1/facility-groups/{facility-group-id}/scope-assignments` (see [Facility Group Scope Assignments API](facility-group-scope-assignments-api.md)) |

### FacilityGroupLocale

| Field         | Type    | Required | Constraints                                                                                             | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                                                               | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation                                                        | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 255 chars, unique among active translations for the same locale (app-level only — no DB constraint) | Localized name of the group                                                    |
| `description` | String  | Yes      | not null (defaults to `""`)                                                                             | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 1                                                                                               | Display order among locale entries                                             |

---

## Create Facility Group

`POST /api/v1/facility-groups`

Creates a new facility group together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted facility groups — attempting to reuse an existing code returns `409 CONFLICT`. This
uniqueness check is application-level only; the underlying `facility_groups` table has no DB-level unique
constraint on `code`. `facility_scope_ids` must be non-empty, and every id in it must reference an existing,
active facility scope — any unknown id returns `404 ENTITY_NOT_FOUND` listing the missing ids.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Facility Group Locales sub-resource below.

### Request Body

```json
{
  "code": "DINING",
  "facility_scope_ids": [
    1
  ],
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  },
  "locale": {
    "name": "Dining",
    "description": "All food and beverage outlets including restaurants, bars, and room service.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field                | Type    | Required | Validation                                                                                 |
|----------------------|---------|----------|--------------------------------------------------------------------------------------------|
| `code`               | String  | Yes      | Not blank, max 100 chars, unique among active records                                      |
| `facility_scope_ids` | Long[]  | Yes      | Not empty; every id must reference an existing facility scope                              |
| `sort_order`         | Integer | Yes      | Not null                                                                                   |
| `icon_type`          | String  | Yes      | Not blank, max 100 chars                                                                   |
| `icon_value`         | String  | No       | —                                                                                          |
| `icon_meta`          | Object  | No       | —, free-form JSON object                                                                   |
| `locale`             | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation                                                          |
|---------------|---------|----------|---------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars, unique among active translations for `en` |
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

## Get Facility Group

`GET /api/v1/facility-groups/{id}`

Returns a single active facility group by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the facility group has no translations at all).
To fetch every translation a facility group has, use
[List Facility Group Locales](#list-facility-group-locales) below.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DINING",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b",
      "stroke_width": 1.5
    },
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Dining",
      "description": "All food and beverage outlets including restaurants, bars, and room service.",
      "sort_order": 1
    },
    "facility_scopes": [
      {
        "id": 1,
        "code": "RESORT",
        "sort_order": 0,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Resort",
          "description": "Applies at the resort level.",
          "sort_order": 0
        }
      }
    ]
  }
}
```

---

## List / Search Facility Groups

`GET /api/v1/facility-groups`

Returns a paginated, filterable list of active (non-deleted) facility groups. All filter parameters are
optional; omitting them returns all facility groups. Each `LIKE`-type filter performs a case-insensitive
partial match. `Accept-Language` selects each facility group's `locale` field the same way as `GET /{id}`
(exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `FacilityGroupFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter          | Type   | Default         | Constraints                                       | Description                                                                                                                            |
|--------------------|--------|-----------------|-----------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| `code`             | String | —               | —                                                 | Filter by code (partial, case-insensitive)                                                                                             |
| `facilityScopeIds` | Long[] | —               | comma-separated                                   | Filter to facility groups assigned to **any** of the given facility scopes (union/OR, not intersection — a group needs only one match) |
| `name`             | String | —               | —                                                 | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale                                             |
| `page`             | int    | `0`             | >= 0                                              | Zero-based page index                                                                                                                   |
| `size`             | int    | `10`            | 1 – 50                                            | Number of items per page                                                                                                                |
| `sortBy`           | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                                                                        |
| `sortDir`          | String | `ASC`           | `ASC`, `DESC`                                     | Sort direction                                                                                                                          |

> **Note:** `sort_order`, `icon_type`, `icon_value`, and `icon_meta` are not filterable or sortable — only
> `code` and locale `name` are wired into the search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "DINING",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b",
        "stroke_width": 1.5
      },
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Dining",
        "description": "All food and beverage outlets including restaurants, bars, and room service.",
        "sort_order": 1
      },
      "facility_scopes": [
        {
          "id": 1,
          "code": "RESORT",
          "sort_order": 0,
          "locale": {
            "id": 1,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Resort",
            "description": "Applies at the resort level.",
            "sort_order": 0
          }
        }
      ]
    },
    {
      "id": 2,
      "code": "WELLNESS",
      "sort_order": 2,
      "icon_type": "LUCIDE",
      "icon_value": "Spa",
      "icon_meta": {
        "size": 24,
        "color": "#8b5cf6",
        "stroke_width": 1.5
      },
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Wellness",
        "description": "Spa, fitness center, and wellness treatment facilities.",
        "sort_order": 2
      },
      "facility_scopes": []
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

## Update Facility Group

`PUT /api/v1/facility-groups/{id}`

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. `code` is set at creation and cannot be
changed. Locale translations are managed separately via the Facility Group Locales sub-resource endpoints
below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Request Body

```json
{
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 32,
    "color": "#f59e0b",
    "stroke_width": 1.5
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `sort_order` | Integer | Yes      | Not null                 |
| `icon_type`  | String  | Yes      | Not blank, max 100 chars |
| `icon_value` | String  | No       | —                        |
| `icon_meta`  | Object  | No       | —, free-form JSON object |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Facility Group

`DELETE /api/v1/facility-groups/{id}`

Soft-deletes the facility group. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Group Locales

Facility Group Locale endpoints manage locale-specific name/description translations for a facility group.
The `{facility-group-id}` path parameter must reference an existing, active facility group.

---

### List Facility Group Locales

`GET /api/v1/facility-groups/{facility-group-id}/locales`

Returns a paginated list of every locale translation belonging to a facility group — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /facility-groups/{id}` and
`GET /facility-groups`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

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
      "name": "Dining",
      "description": "All food and beverage outlets including restaurants, bars, and room service.",
      "sort_order": 1
    },
    {
      "id": 5,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "খাবার",
      "description": "রেস্টুরেন্ট, বার এবং রুম সার্ভিসসহ সকল খাদ্য ও পানীয় আউটলেট।",
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

### Count Facility Group Locales

`GET /api/v1/facility-groups/{facility-group-id}/locales/count`

Returns how many active locale translations a facility group currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the facility group is still missing and can add a translation
for via [Create Facility Group Locale](#create-facility-group-locale) — e.g. if the platform has `en`,
`bn`, `es` and this endpoint returns `en`, `bn` for the facility group, `es` is still available; if it
returns all three, every platform locale already has a translation and `POST .../locales` for any of them
will fail with `409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

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

### Create Facility Group Locale

`POST /api/v1/facility-groups/{facility-group-id}/locales`

Adds a new locale translation to an existing facility group. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility group and locale
must be unique — adding a locale the facility group already has a translation for returns `409 CONFLICT`.
`name` must also be unique among active translations for the same locale, regardless of which facility group
they belong to — reusing a name already in use for that locale returns `409 CONFLICT`. Both checks are
application-level only; the underlying `facility_group_locales` table has no DB-level unique constraints
backing either one.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "খাবার",
  "description": "রেস্টুরেন্ট, বার এবং রুম সার্ভিসসহ সকল খাদ্য ও পানীয় আউটলেট।",
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
  "id": 5
}
```

---

### Update Facility Group Locale

`PUT /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing facility group locale translation. The
associated facility group and locale cannot be changed after creation. `name` is re-checked for uniqueness
among active translations for the same locale, excluding this translation itself — renaming it to a name
already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility group locale |

#### Request Body

```json
{
  "name": "খাবার",
  "description": "রেস্টুরেন্ট, বার এবং রুম সার্ভিসসহ সকল খাদ্য ও পানীয় আউটলেট।",
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
  "id": 5
}
```

---

### Delete Facility Group Locale

`DELETE /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Soft-deletes a facility group locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility group locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
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
  "message": "FacilityGroup not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                                                                                                                        |
|-------------|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                                        |
| 404         | `ENTITY_NOT_FOUND` | Facility group not found, facility group locale not found, any facility scope referenced in `facility_scope_ids` not found (`create`), or the locale referenced by `locale_id` not found (locale creation)                                                                                   |
| 409         | `CONFLICT`         | `code` already in use by another active facility group (`create`); the facility group already has a translation for the given `locale_id` (`create` locale, pre-checked); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
