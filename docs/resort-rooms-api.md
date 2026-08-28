# Resort Rooms API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms`

A Resort Room is one physical room belonging to a [Resort Room Category](resort-room-categories-api.md) — the
category groups rooms that share the same occupancy/pricing profile (e.g. "Deluxe Sea View"), while each room
underneath it has its own resort-scoped `code`, an operational `room_status` (e.g. `AVAILABLE`,
`MAINTENANCE`), and optional free-form physical location (`floor_number`, `building`). A resort room's display
name and description are locale-specific and are managed through a companion sub-resource — Resort Room
Locales — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales`. Its
occupancy, room-detail, and booking-rule settings live in a **1:1 Meta sub-resource**, created automatically
together with the resort room and updated separately via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/meta`. Its bed
configuration (one row per bed type, e.g. 1 king + 1 sofa bed) lives in a **1:N Beds sub-resource** — [Resort
Room Beds](resort-room-beds-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`. At least
one bed is required at creation time (see `beds` below); further beds are added afterward through that
sub-resource. Unlike locale and meta, a room's bed rows are embedded directly as the `beds` array on this
entity's own `GET` responses (see [Data Model](#data-model) below) in addition to being manageable through the
sub-resource.

**`room_status` is deliberately kept out of the general update endpoint.** [Update Resort
Room](#update-resort-room) only changes `sort_order`/`floor_number`/`building` — a room's operational status is
changed exclusively through [Update Resort Room Status](#update-resort-room-status), a separate endpoint. This
keeps a routine field edit from accidentally flipping a room's availability, and gives status transitions their
own place to grow independent business rules later.

All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Resort Room)** and **`GET` (List Resort Rooms)** — the header's value selects exactly
  one locale translation for the resort room's `locale` field (and for the nested `room_status.locale`
  field): an exact match if a translation exists, otherwise `en`, otherwise `null`.
- **`GET .../locales` (List Resort Room Locales)** — the header must be present, but its value has no
  effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** (including the Meta sub-resource's `PUT` and the status `PUT`) — the header must
  be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                                                       | Description                    |
|--------|-------------------------------------------------------------------------------------------------------------|---------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms`                                | Create a resort room            |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`                           | Get a resort room                |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms`                                | List resort rooms                |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`                           | Update a resort room             |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}/status`                    | Update a resort room's status    |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`                           | Delete a resort room             |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/meta`          | Update a resort room's meta      |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales`       | List a resort room's locales     |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/count` | Count a resort room's used platform locales |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales`       | Create a resort room locale      |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/{id}`  | Update a resort room locale      |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/{id}`  | Delete a resort room locale      |

Resort room beds have their own full endpoint set — see the [Resort Room Beds API](resort-room-beds-api.md) —
reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`.

Resort room facility groups and facilities also have their own full endpoint sets — see the
[Resort Room Facility Groups API](resort-room-facility-groups-api.md) and
[Resort Room Facilities API](resort-room-facilities-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`
and
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`
respectively. These mirror [Resort Room Category Facility
Groups](resort-room-category-facility-groups-api.md) and [Resort Room Category
Facilities](resort-room-category-facilities-api.md) one level down the hierarchy (per individual room instead
of per room category).

---

## Data Model

### ResortRoom

| Field                   | Type    | Required | Constraints                                                                                  | Description                                                                                                                       |
|-------------------------|---------|----------|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| `id`                    | Long    | —        | read-only                                                                                     | Auto-generated identifier                                                                                                            |
| `code`                  | String  | Yes      | max 50 chars, unique per resort among active records; set at creation, immutable             | Resort-scoped identifier for this room (e.g. `R-101`). Uniqueness spans the whole resort (reached via `resort_room_category`), not just this room category |
| `sort_order`            | Integer | Yes      | default 0                                                                                     | Display order                                                                                                                        |
| `floor_number`          | Integer | —        | nullable                                                                                      | Free-form floor number — not a fixed vocabulary, so no lookup table                                                                  |
| `building`              | String  | —        | nullable, max 100 chars                                                                       | Free-form building name/identifier — not a fixed vocabulary, so no lookup table                                                      |
| `resort_room_category`  | Object  | —        | read-only; see [ResortRoomCategory](resort-room-categories-api.md)                            | The parent resort room category (`id`, `code`, `sort_order`). Resolved from the URL path, never a request body field                |
| `room_status`           | Object  | —        | read-only; see [RoomStatus](room-statuses-api.md); resolved from `room_status_id`             | The room's current operational status. Set at creation; changed afterward only via [Update Resort Room Status](#update-resort-room-status), never via [Update Resort Room](#update-resort-room) |
| `locale`                | Object  | —        | nullable; see ResortRoomLocale below                                                          | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if there are no translations at all) |
| `meta`                  | Object  | —        | read-only here; see ResortRoomMeta below                                                       | Occupancy, room-detail, and booking-rule settings — always present, created automatically with the resort room                       |
| `beds`                  | Array   | —        | read-only here; see [ResortRoomBed](resort-room-beds-api.md#data-model)                       | Bed configuration rows (one per bed type) — always at least one, created together with the resort room                              |

### ResortRoomLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|---------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized room name (e.g. "Room 101")                                         |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                            |

### ResortRoomMeta

| Field                 | Type    | Required | Constraints                                                         | Description                                                                                                                |
|-----------------------|---------|----------|-----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| `id`                  | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                    |
| `max_adults`          | Integer | Yes      | default 2                                                             | Maximum number of adults                                                                                                     |
| `max_children`        | Integer | Yes      | default 0                                                             | Maximum number of children                                                                                                   |
| `max_infants`         | Integer | Yes      | default 0                                                             | Maximum number of infants                                                                                                    |
| `max_occupancy`       | Integer | Yes      | default 2; must be >= `max_adults` + `max_children` + `max_infants`   | Maximum total occupancy                                                                                                       |
| `room_size`           | Decimal | —        | nullable; > 0 if not null                                             | Room size, in the unit given by `room_size_unit`                                                                             |
| `room_size_unit`      | Object  | —        | read-only; nullable; see [Unit](units-api.md#data-model)              | The unit `room_size` is measured in (e.g. sqm, sqft); set at write time via `room_size_unit_id` (see Request Fields below)  |
| `bedroom_count`       | Integer | Yes      | default 1; > 0                                                        | Number of bedrooms                                                                                                            |
| `bathroom_count`      | Integer | Yes      | default 1; > 0                                                        | Number of bathrooms                                                                                                          |
| `minimum_stay_nights` | Integer | Yes      | default 1; > 0                                                        | Minimum stay length, in nights                                                                                               |
| `maximum_stay_nights` | Integer | —        | nullable; if not null, must be >= `minimum_stay_nights`                | Maximum stay length, in nights                                                                                               |

`ResortRoomBed` (the shape of each entry in `beds`) is documented in full in the [Resort Room Beds
API](resort-room-beds-api.md#data-model), including its own dedicated endpoints.

---

## Create Resort Room

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms`

Creates a new resort room under the given resort room category, together with its initial `room_status`,
exactly **one** initial locale translation, and its **meta** settings. `code` must be unique among active,
non-deleted rooms for the whole resort (not just this category) — attempting to reuse an existing code
returns `409 CONFLICT` at the application level, backstopped by a DB trigger.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional
languages are added afterward via the Resort Room Locales sub-resource below.

**`meta` is required** — every resort room always has meta settings, created in the same transaction. There
is no separate `POST` for meta; afterward it is only ever changed via [Update Resort Room
Meta](#update-resort-room-meta) below.

**`beds` is required and must contain at least one entry** — every resort room must be created with at least
one bed configuration row. `bed_type_id` must be unique across the entries in a single request — a duplicate
returns `409 CONFLICT` — and each must reference an existing, active bed type. Each entry has the same shape
as [Create Resort Room Bed](resort-room-beds-api.md#create-resort-room-bed), minus the resort room (resolved
from the URL path there instead). Additional beds can be added afterward via that same endpoint.

### Path Parameters

| Parameter                 | Type | Description                    |
|----------------------------|------|-----------------------------------|
| `resort-id`                | Long | ID of the resort                  |
| `resort-room-category-id`  | Long | ID of the parent resort room category |

### Request Body

```json
{
  "room_status_id": 1,
  "code": "R-101",
  "sort_order": 1,
  "floor_number": 1,
  "building": "Main Building",
  "locale": {
    "name": "Room 101",
    "description": "Deluxe sea view room on the first floor.",
    "sort_order": 1
  },
  "meta": {
    "max_adults": 2,
    "max_children": 1,
    "max_infants": 0,
    "max_occupancy": 3,
    "room_size": 45.50,
    "room_size_unit_id": 1,
    "bedroom_count": 1,
    "bathroom_count": 1,
    "minimum_stay_nights": 1,
    "maximum_stay_nights": 14
  },
  "beds": [
    {
      "bed_type_id": 3,
      "quantity": 1,
      "is_extra_bed_allowed": true,
      "max_extra_beds": 1
    }
  ]
}
```

### Request Fields

| Field             | Type    | Required | Validation                                                                                                                 |
|--------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------------|
| `room_status_id`   | Long    | Yes      | Not null; must reference an existing, active room status. Changed afterward only via [Update Resort Room Status](#update-resort-room-status) |
| `code`             | String  | Yes      | Not blank, max 50 chars, unique among this resort's active rooms (across every room category)                              |
| `sort_order`       | Integer | Yes      | Not null                                                                                                                       |
| `floor_number`     | Integer | —        | Nullable                                                                                                                       |
| `building`         | String  | —        | Nullable, max 100 chars                                                                                                        |
| `locale`           | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale                                    |
| `meta`             | Object  | Yes      | Not null; validated (see below)                                                                                               |
| `beds`             | Array   | Yes      | Not empty; each entry validated (see below); `bed_type_id` must be unique within the array (`409 CONFLICT` if duplicated)     |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

**Meta entry (`meta`):**

| Field                 | Type    | Required | Validation                                              |
|-----------------------|---------|----------|-----------------------------------------------------------|
| `max_adults`          | Integer | Yes      | Not null                                                   |
| `max_children`        | Integer | Yes      | Not null                                                   |
| `max_infants`         | Integer | Yes      | Not null                                                   |
| `max_occupancy`       | Integer | Yes      | Not null                                                   |
| `room_size`           | Decimal | —        | Nullable; > 0 if supplied                                  |
| `room_size_unit_id`   | Long    | —        | Nullable; must reference an existing unit if supplied      |
| `bedroom_count`       | Integer | Yes      | Not null; > 0                                              |
| `bathroom_count`      | Integer | Yes      | Not null; > 0                                              |
| `minimum_stay_nights` | Integer | Yes      | Not null; > 0                                              |
| `maximum_stay_nights` | Integer | —        | Nullable; if supplied, must be >= `minimum_stay_nights`    |

> **Note:** only the "Not null" checks above are enforced as request-validation errors (`400 INVALID_ARGUMENT`).
> The numeric rules (`> 0`, the `max_occupancy >= max_adults + max_children + max_infants` relationship, and
> `maximum_stay_nights >= minimum_stay_nights`) are enforced solely by database CHECK constraints, not by
> request validation — a request that violates one of them passes validation and fails at the DB layer with
> `409 DATA_INTEGRITY_VIOLATION` instead (see [Error Responses](#error-responses)).

**Bed entry (each item in `beds`):**

| Field                  | Type    | Required | Validation                                                                                            |
|------------------------|---------|----------|-------------------------------------------------------------------------------------------------------|
| `bed_type_id`          | Long    | Yes      | Not null; must reference an existing bed type; unique within the array (`409 CONFLICT` if duplicated) |
| `quantity`             | Integer | Yes      | Not null; >= 1                                                                                         |
| `is_extra_bed_allowed` | Boolean | Yes      | Not null                                                                                                |
| `max_extra_beds`       | Integer | Yes      | Not null; >= 0                                                                                          |

### Response `201 Created`

```json
{
  "success": true,
  "id": 20
}
```

---

## Get Resort Room

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`

Returns a single active resort room by its ID, scoped to its owning resort room category — an `id` that
exists but belongs to a different resort room category returns `404 ENTITY_NOT_FOUND`. `locale` is the one
translation matching the request's `Accept-Language` header (falls back to `en`, then `null` if there are no
translations at all). To fetch every translation, use [List Resort Room Locales](#list-resort-room-locales)
below.

### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `id`                        | Long | ID of the resort room                       |

### Response `200 OK`

```json
{
  "data": {
    "id": 20,
    "code": "R-101",
    "sort_order": 1,
    "floor_number": 1,
    "building": "Main Building",
    "resort_room_category": {
      "id": 10,
      "code": "DLX-SEA",
      "sort_order": 1
    },
    "room_status": {
      "id": 1,
      "code": "AVAILABLE",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Available",
        "description": "",
        "sort_order": 1
      }
    },
    "locale": {
      "id": 30,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Room 101",
      "description": "Deluxe sea view room on the first floor.",
      "sort_order": 1
    },
    "meta": {
      "id": 15,
      "max_adults": 2,
      "max_children": 1,
      "max_infants": 0,
      "max_occupancy": 3,
      "room_size": 45.50,
      "room_size_unit": {
        "id": 1,
        "code": "SQM",
        "symbol": "m²",
        "is_base_unit": true,
        "conversion_factor": 1,
        "sort_order": 0,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Square Meter",
          "plural_name": "Square Meters",
          "description": "Metric unit of area",
          "sort_order": 1
        }
      },
      "bedroom_count": 1,
      "bathroom_count": 1,
      "minimum_stay_nights": 1,
      "maximum_stay_nights": 14
    },
    "beds": [
      {
        "id": 25,
        "bed_type": {
          "id": 3,
          "code": "KING",
          "sort_order": 1,
          "locale": {
            "id": 5,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "King Bed",
            "description": "",
            "sort_order": 1
          }
        },
        "quantity": 1,
        "is_extra_bed_allowed": true,
        "max_extra_beds": 1
      }
    ]
  }
}
```

---

## List Resort Rooms

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms`

Returns a paginated list of active (non-deleted) resort rooms belonging to the given resort room category.
`Accept-Language` selects each row's `locale` field (and the nested `room_status.locale` field) the same way
as `GET /{id}` (exact match, falls back to `en`, then `null`). Each row also embeds its full `meta` object and
`beds` array, same as `GET /{id}`.

> **Note:** no field is currently registered as filterable or sortable beyond the default. `sortBy` accepts
> only `createdAt` — passing any other value throws `400 INVALID_ARGUMENT: Invalid sort field: <value>`.
> Omitting `sortBy` entirely sorts by `id` (implicit).

### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |

### Query Parameters

| Parameter | Type   | Default         | Constraints      | Description              |
|-----------|--------|-----------------|------------------|---------------------------|
| `page`    | int    | `0`             | >= 0             | Zero-based page index      |
| `size`    | int    | `10`            | 1 – 50           | Number of items per page   |
| `sortBy`  | String | `id` (implicit) | `createdAt` only | Field to sort by           |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`    | Sort direction              |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 20,
      "code": "R-101",
      "sort_order": 1,
      "floor_number": 1,
      "building": "Main Building",
      "resort_room_category": {
        "id": 10,
        "code": "DLX-SEA",
        "sort_order": 1
      },
      "room_status": {
        "id": 1,
        "code": "AVAILABLE",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Available",
          "description": "",
          "sort_order": 1
        }
      },
      "locale": {
        "id": 30,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Room 101",
        "description": "Deluxe sea view room on the first floor.",
        "sort_order": 1
      },
      "meta": {
        "id": 15,
        "max_adults": 2,
        "max_children": 1,
        "max_infants": 0,
        "max_occupancy": 3,
        "room_size": 45.50,
        "room_size_unit": {
          "id": 1,
          "code": "SQM",
          "symbol": "m²",
          "is_base_unit": true,
          "conversion_factor": 1,
          "sort_order": 0,
          "locale": {
            "id": 1,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Square Meter",
            "plural_name": "Square Meters",
            "description": "Metric unit of area",
            "sort_order": 1
          }
        },
        "bedroom_count": 1,
        "bathroom_count": 1,
        "minimum_stay_nights": 1,
        "maximum_stay_nights": 14
      },
      "beds": [
        {
          "id": 25,
          "bed_type": {
            "id": 3,
            "code": "KING",
            "sort_order": 1,
            "locale": {
              "id": 5,
              "locale": {
                "id": 1,
                "code": "en",
                "name": "English",
                "sort_order": 1
              },
              "name": "King Bed",
              "description": "",
              "sort_order": 1
            }
          },
          "quantity": 1,
          "is_extra_bed_allowed": true,
          "max_extra_beds": 1
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
    "createdAt"
  ],
  "searchable_fields": []
}
```

---

## Update Resort Room

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`

Updates `sort_order`, `floor_number`, and `building` only. `code` is set at creation and cannot be changed.
`room_status` is **not** updatable here — see [Update Resort Room Status](#update-resort-room-status) below.
Locale translations and meta settings are managed separately, via the sub-resource endpoints below, not
through this endpoint.

### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `id`                        | Long | ID of the resort room                       |

### Request Body

```json
{
  "sort_order": 2,
  "floor_number": 2,
  "building": "Main Building"
}
```

### Request Fields

| Field          | Type    | Required | Validation           |
|-----------------|---------|----------|--------------------------|
| `sort_order`    | Integer | Yes      | Not null                  |
| `floor_number`  | Integer | —        | Nullable                  |
| `building`      | String  | —        | Nullable, max 100 chars   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 20
}
```

---

## Update Resort Room Status

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}/status`

Changes a resort room's `room_status` only — the room's only path to a status transition (e.g. `AVAILABLE` ->
`MAINTENANCE`). Kept deliberately separate from [Update Resort Room](#update-resort-room) above so that a
routine field edit can never accidentally change availability, and so status transitions have their own place
to grow independent business rules later. If the submitted `room_status_id` is the same as the room's current
status, this is a no-op that still returns `200 OK`.

### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `id`                        | Long | ID of the resort room                       |

### Request Body

```json
{
  "room_status_id": 2
}
```

### Request Fields

| Field             | Type | Required | Validation                                                 |
|--------------------|------|----------|------------------------------------------------------------|
| `room_status_id`   | Long | Yes      | Not null; must reference an existing, active room status   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 20
}
```

---

## Delete Resort Room

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{id}`

Soft-deletes the resort room (and cascades the soft-delete to its own locale translations, its meta row, and
its bed rows). The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `id`                        | Long | ID of the resort room                       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 20
}
```

---

## Resort Room Meta

The Meta sub-resource holds occupancy, room-detail, and booking-rule settings for a resort room. It is a
strict **1:1** relationship — every resort room has exactly one meta row, created automatically by [Create
Resort Room](#create-resort-room) above. There is no `POST`, standalone `GET`, or `DELETE` for meta — it is
only ever read as the embedded `meta` field on [Get](#get-resort-room)/[List](#list-resort-rooms) Resort Room,
and only ever changed via the `PUT` below.

---

### Update Resort Room Meta

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/meta`

Updates every field of a resort room's meta settings (full replace, not a partial patch — omitted fields are
rejected by validation the same as on create, except the two nullable fields).

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |

#### Request Body

```json
{
  "max_adults": 2,
  "max_children": 2,
  "max_infants": 1,
  "max_occupancy": 4,
  "room_size": 52.00,
  "room_size_unit_id": 1,
  "bedroom_count": 1,
  "bathroom_count": 1,
  "minimum_stay_nights": 2,
  "maximum_stay_nights": 21
}
```

#### Request Fields

Same shape as the `meta` entry on [Create Resort Room](#create-resort-room) — see the Meta entry table there.

#### Response `200 OK`

```json
{
  "success": true,
  "id": 15
}
```

`id` is the meta row's own id, not the resort room's.

---

## Resort Room Locales

Resort Room Locale endpoints manage locale-specific name/description translations for a resort room. The
`{resort-id}`, `{resort-room-category-id}`, and `{resort-room-id}` path parameters must reference an existing,
active resort, resort room category, and resort room respectively.

---

### List Resort Room Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales`

Returns a paginated list of every locale translation belonging to a resort room — this is the only way to see
more than the single Accept-Language-matched translation returned by `GET .../rooms/{id}` and
`GET .../rooms`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-----------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn`     |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                                |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                             |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 30,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Room 101",
      "description": "Deluxe sea view room on the first floor.",
      "sort_order": 1
    },
    {
      "id": 31,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "রুম ১০১",
      "description": "",
      "sort_order": 2
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

### Count Resort Room Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes this resort room already has an
active translation for, together with each one's `code`. Matched via `locale_id`. `count` is always
`codes.length` — both come from the same query, so there's no separate tally to drift out of sync with the
list. Use this to gray out/disable locales already present in `codes` when building the picker for [Create
Resort Room Locale](#create-resort-room-locale) — `locale_id` must not already have a translation for this
resort room, or the create call returns `409 CONFLICT`.

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |

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

### Create Resort Room Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales`

Adds a new locale translation to an existing resort room. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort room and locale must
be unique — adding a locale it already has a translation for returns `409 CONFLICT`.

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রুম ১০১",
  "description": "",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|-----------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale     |
| `name`        | String  | Yes      | Not blank, max 150 chars                        |
| `description` | String  | Yes      | Not null                                        |
| `sort_order`  | Integer | Yes      | Not null                                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 31
}
```

---

### Update Resort Room Locale

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing resort room locale translation. The associated
resort room and locale cannot be changed after creation.

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |
| `id`                        | Long | ID of the resort room locale                |

#### Request Body

```json
{
  "name": "রুম ১০১",
  "description": "",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 31
}
```

---

### Delete Resort Room Locale

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/locales/{id}`

Soft-deletes a resort room locale. The record is not removed from the database but will no longer appear in
any response.

#### Path Parameters

| Parameter                 | Type | Description                            |
|----------------------------|------|--------------------------------------------|
| `resort-id`                | Long | ID of the resort                            |
| `resort-room-category-id`  | Long | ID of the parent resort room category       |
| `resort-room-id`           | Long | ID of the parent resort room                |
| `id`                        | Long | ID of the resort room locale                |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 31
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
  "message": "ResortRoom not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|-------------|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields (including an empty `beds` array); or an unsupported `sortBy` query value. **Note:** none of the `meta` numeric rules (`> 0` bounds, `max_occupancy` vs. adults/children/infants, `maximum_stay_nights` vs. `minimum_stay_nights`) are bean-validated — violating one of those does *not* produce this error; see the `409 DATA_INTEGRITY_VIOLATION` row below                    |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, resort room category not found (including one that belongs to a different resort), room status not found (`room_status_id`, at create or via [Update Resort Room Status](#update-resort-room-status)), unit not found (`room_size_unit_id` if supplied), bed type not found (any `bed_type_id` in `beds` at create), resort room not found (including one that belongs to a different resort room category), resort room meta not found, resort room locale not found, or the locale referenced by `locale_id` not found (locale creation) |
| 409         | `CONFLICT`                 | `code` already in use by another active resort room for this resort (`create`, pre-checked at the application level); a duplicate `bed_type_id` within `beds` at create (pre-checked at the application level); or the resort room already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level)                                                                                                                                                                                  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraints on `resort_room_id` (meta), `(resort_room_id, locale_id)`, or the beds sub-resource's own constraint described in the [Resort Room Beds API](resort-room-beds-api.md#error-responses) — should not normally be reachable now that duplicates are pre-checked at the application level; **also the actual enforcement point** for every `meta` numeric CHECK constraint (`room_size > 0`, `bedroom_count > 0`, `bathroom_count > 0`, `minimum_stay_nights > 0`, `max_occupancy >= max_adults + max_children + max_infants`, `maximum_stay_nights >= minimum_stay_nights`) on both create and [Update Resort Room Meta](#update-resort-room-meta), since none of those rules are bean-validated |
| 409         | `DATABASE_RULE_VIOLATION`  | Last-resort backstop for `code` uniqueness — unlike `ResortRoomCategory.code` (a plain DB unique index), `ResortRoom.code`'s uniqueness spans the whole resort (reached via `resort_room_category_id -> resort_id`, which a plain index can't express), so it's enforced by the `fn_validate_resort_room_code_unique_per_resort` Postgres trigger in addition to the application-level check above — should not normally be reachable                                                                                            |

See the [Resort Room Beds API](resort-room-beds-api.md#error-responses) for errors specific to the standalone
beds sub-resource endpoints (e.g. the `409 CONFLICT` when a bed type is already in use).
