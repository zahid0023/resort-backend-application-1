# Facilities API

Base URL: `/api/v1/facilities`

Facilities are the individual amenities a resort offers (e.g. `RESTAURANT`, `SPA`, `SWIMMING_POOL`), each
belonging to one or more facility groups (`DINING`, `WELLNESS`, ...) and one or more facility scopes (`RESORT`,
`ROOM_CATEGORY`, `ROOM`), and carrying its own icon. A facility must belong to at least one facility group and
at least one facility scope at creation time. Group membership afterward is managed via the
[Facility Group Assignments API](facility-group-assignments-api.md); scope membership afterward is managed via
the [Facility Scope Assignments API](facility-scope-assignments-api.md) — neither is managed through this API.
A facility's display name and description are locale-specific and are managed
through a companion sub-resource — Facility Locales — reached via `/api/v1/facilities/{facility-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Facility)** and **`GET` (List/Search Facilities)** — the header's value selects exactly
  one locale translation for the facility's `locale` field: an exact match if the facility has one,
  otherwise `en`, otherwise `null`.
- **`GET /{facility-id}/locales` (List Facility Locales)** — the header must be present, but its value has
  no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                             | Description                |
|--------|--------------------------------------------------|----------------------------|
| POST   | `/api/v1/facilities`                             | Create a facility          |
| GET    | `/api/v1/facilities`                             | List / search facilities   |
| GET    | `/api/v1/facilities/{id}`                        | Get a facility             |
| PUT    | `/api/v1/facilities/{id}`                        | Update a facility          |
| DELETE | `/api/v1/facilities/{id}`                        | Delete a facility          |
| GET    | `/api/v1/facilities/{facility-id}/locales`       | List a facility's locales  |
| GET    | `/api/v1/facilities/{facility-id}/locales/count` | Count a facility's locales |
| POST   | `/api/v1/facilities/{facility-id}/locales`       | Create a facility locale   |
| PUT    | `/api/v1/facilities/{facility-id}/locales/{id}`  | Update a facility locale   |
| DELETE | `/api/v1/facilities/{facility-id}/locales/{id}`  | Delete a facility locale   |

---

## Data Model

### Facility

| Field             | Type    | Required | Constraints                                                                    | Description                                                                                                                                                                           |
|-------------------|---------|----------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`              | Long    | —        | read-only                                                                      | Auto-generated identifier                                                                                                                                                             |
| `facility_groups` | Array   | —        | read-only                                                                      | The facility groups this facility belongs to — each same shape as [Facility Groups](facility-groups-api.md)'s `FacilityGroup` data model; always at least one                         |
| `code`            | String  | Yes      | max 100 chars, unique among active records; set at creation, immutable         | Internal code (e.g. `RESTAURANT`, `SPA`)                                                                                                                                              |
| `sort_order`      | Integer | Yes      | default 0                                                                      | Display order                                                                                                                                                                         |
| `icon_type`       | String  | Yes      | max 100 chars                                                                  | Icon library/source (e.g. `LUCIDE`)                                                                                                                                                   |
| `icon_value`      | String  | No       | nullable                                                                       | Icon name/path within `icon_type`'s library                                                                                                                                           |
| `icon_meta`       | Object  | No       | nullable, free-form JSON                                                       | Icon rendering metadata (e.g. `{"size": 24, "color": "#f59e0b"}`)                                                                                                                     |
| `locale`          | Object  | —        | nullable; see FacilityLocale below                                             | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the facility has no translations at all)                                          |
| `facility_scopes` | Array   | —        | read-only; see Facility Scope in [Facility Scopes API](facility-scopes-api.md) | The facility scopes currently assigned to this facility, managed via `POST/DELETE /api/v1/facilities/{facility-id}/scope-assignments` (see [Facility Scope Assignments API](facility-scope-assignments-api.md)) |

### FacilityLocale

| Field         | Type    | Required | Constraints                                                         | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                           | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation                    | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 255 chars, unique among active translations for the same locale | Localized name of the facility                                                 |
| `description` | String  | Yes      | not null (defaults to `""`)                                         | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                                           | Display order among locale entries                                             |

---

## Create Facility

`POST /api/v1/facilities`

Creates a new facility together with exactly **one** initial locale translation. `code` must be unique among
active, non-deleted facilities — attempting to reuse an existing code returns `409 CONFLICT`.
`facility_group_ids` and `facility_scope_ids` must each be non-empty, and every id in either must reference an
existing, active record of the matching type — any unknown id returns `404 ENTITY_NOT_FOUND` listing the
missing ids.

**Every facility group in `facility_group_ids` must itself be scoped (see
[Facility Group Scope Assignments](facility-group-scope-assignments-api.md)) to every scope in
`facility_scope_ids`.** For example, a facility requesting `RESORT` cannot be placed in a facility group that
is only assigned to `ROOM_CATEGORY`/`ROOM` scopes. This check runs per facility group — if the facility
belongs to multiple groups, each one individually must support the full set of requested scopes. Violating
this returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Facility Locales sub-resource below.

### Request Body

```json
{
  "code": "RESTAURANT",
  "facility_group_ids": [
    1
  ],
  "facility_scope_ids": [
    1
  ],
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b"
  },
  "locale": {
    "name": "Main Restaurant",
    "description": "Full-service restaurant with buffet and à la carte options.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field                | Type    | Required | Validation                                                                                 |
|----------------------|---------|----------|--------------------------------------------------------------------------------------------|
| `code`               | String  | Yes      | Not blank, max 100 chars, unique among active records                                      |
| `facility_group_ids` | Long[]  | Yes      | Not empty; every id must reference an existing facility group                              |
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

## Get Facility

`GET /api/v1/facilities/{id}`

Returns a single active facility by its ID, with its `facility_groups` and assigned `facility_scopes`
embedded. `locale` is the one translation matching the request's `Accept-Language` header (falls back to `en`,
then `null` if the facility has no translations at all). To fetch every translation a facility has, use
[List Facility Locales](#list-facility-locales) below.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "facility_groups": [
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
        }
      }
    ],
    "code": "RESTAURANT",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b"
    },
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Main Restaurant",
      "description": "Full-service restaurant with buffet and à la carte options.",
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

## List / Search Facilities

`GET /api/v1/facilities`

Returns a paginated, filterable list of active (non-deleted) facilities, each with its `facility_groups` and
assigned `facility_scopes` embedded. All filter parameters are optional; omitting them returns all facilities.
Each `LIKE`-type filter performs a case-insensitive partial match. `Accept-Language` selects each facility's
`locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `FacilityFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter         | Type   | Default         | Constraints                                                    | Description                                                                                           |
|-------------------|--------|-----------------|----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| `code`            | String | —               | —                                                              | Filter by code (partial, case-insensitive)                                                            |
| `facilityGroupId` | Long   | —               | —                                                              | Filter to facilities that belong to the given facility group (a facility may belong to more than one) |
| `name`            | String | —               | —                                                              | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale             |
| `page`            | int    | `0`             | >= 0                                                           | Zero-based page index                                                                                 |
| `size`            | int    | `10`            | 1 – 50                                                         | Number of items per page                                                                              |
| `sortBy`          | String | `id` (implicit) | `createdAt`, `sortOrder`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                                      |
| `sortDir`         | String | `ASC`           | `ASC`, `DESC`                                                  | Sort direction                                                                                        |

> **Note:** `icon_type`, `icon_value`, and `icon_meta` are not filterable or sortable.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "facility_groups": [
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
          }
        }
      ],
      "code": "RESTAURANT",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
      },
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Main Restaurant",
        "description": "Full-service restaurant with buffet and à la carte options.",
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
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "sortOrder",
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

## Update Facility

`PUT /api/v1/facilities/{id}`

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. `code` is set at creation and cannot be
changed. Facility group membership is managed separately via the
[Facility Group Assignments API](facility-group-assignments-api.md), not through this
endpoint. Locale translations are managed separately via the Facility Locales sub-resource endpoints below.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Request Body

```json
{
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 32,
    "color": "#f59e0b"
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

## Delete Facility

`DELETE /api/v1/facilities/{id}`

Soft-deletes the facility. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Locales

Facility Locale endpoints manage locale-specific name/description translations for a facility. The
`{facility-id}` path parameter must reference an existing, active facility.

---

### List Facility Locales

`GET /api/v1/facilities/{facility-id}/locales`

Returns a paginated list of every locale translation belonging to a facility — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET /facilities/{id}` and
`GET /facilities`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the parent facility |

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
      "name": "Main Restaurant",
      "description": "Full-service restaurant with buffet and à la carte options.",
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

### Count Facility Locales

`GET /api/v1/facilities/{facility-id}/locales/count`

Returns how many active locale translations a facility currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the facility is still missing and can add a translation for
via [Create Facility Locale](#create-facility-locale) — e.g. if the platform has `en`, `bn`, `es` and this
endpoint returns `en`, `bn` for the facility, `es` is still available; if it returns all three, every
platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the parent facility |

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

### Create Facility Locale

`POST /api/v1/facilities/{facility-id}/locales`

Adds a new locale translation to an existing facility. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility and locale must
be unique — adding a locale the facility already has a translation for returns `409 CONFLICT`. `name` must
also be unique among active translations for the same locale, regardless of which facility they belong to —
reusing a name already in use for that locale returns `409 CONFLICT`. Both checks are application-level
only; the underlying `facility_locales` table has no DB-level unique constraints backing either one.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the parent facility |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "প্রধান রেস্টুরেন্ট",
  "description": "বুফে এবং আ লা কার্ট অপশন সহ ফুল-সার্ভিস রেস্টুরেন্ট।",
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
  "id": 2
}
```

---

### Update Facility Locale

`PUT /api/v1/facilities/{facility-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing facility locale translation. The associated
facility and locale cannot be changed after creation. `name` is re-checked for uniqueness among active
translations for the same locale, excluding this translation itself — renaming it to a name already used by
another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the parent facility |
| `id`          | Long | ID of the facility locale |

#### Request Body

```json
{
  "name": "প্রধান রেস্টুরেন্ট",
  "description": "বুফে এবং আ লা কার্ট অপশন সহ ফুল-সার্ভিস রেস্টুরেন্ট।",
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
  "id": 2
}
```

---

### Delete Facility Locale

`DELETE /api/v1/facilities/{facility-id}/locales/{id}`

Soft-deletes a facility locale. The record is not removed from the database but will no longer appear in
any response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the parent facility |
| `id`          | Long | ID of the facility locale |

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
  "message": "Facility not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                                                                                                            |
|-------------|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); missing/invalid required fields; or an unsupported `sortBy` query value                                                                                                                  |
| 404         | `ENTITY_NOT_FOUND` | Facility not found, facility locale not found, any facility group referenced in `facility_group_ids` not found (`create`), any facility scope referenced in `facility_scope_ids` not found (`create`), or the locale referenced by `locale_id` not found (locale creation)       |
| 409         | `CONFLICT`         | `code` already in use by another active facility (`create`); a facility group in `facility_group_ids` is not scoped to every scope in `facility_scope_ids` (`create`); the facility already has a translation for the given `locale_id` (`create` locale, pre-checked); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
