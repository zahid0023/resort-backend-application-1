# Resort Room Beds API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`

A resort room bed row describes one bed configuration for a [Resort Room](resort-rooms-api.md) — a platform
[Bed Type](bed-types-api.md) (`bed_type`, e.g. `KING`/`QUEEN`/`SOFA_BED`) plus a `quantity`, and optional
extra-bed rules (`is_extra_bed_allowed`, `max_extra_beds`). A room typically has more than one row — e.g. 1
king bed + 1 sofa bed for the same room.

**Bed rows are an optional, full-replacement override of the room's [Resort Room Category
Beds](resort-room-category-beds-api.md)** — a room needs no bed rows of its own. `beds` is optional on [Create Resort Room](resort-rooms-api.md#create-resort-room); each entry there has
the same shape as [Create Resort Room Bed](#create-resort-room-bed) below, minus the resort room (resolved
from the URL path there instead). The moment a room has even one of its own active bed rows — whether added at
creation or afterward via [Create Resort Room Bed](#create-resort-room-bed) — [List Resort Room
Beds](#list-resort-room-beds) shows the room's own rows exclusively; the category's beds are used only when
the room has zero of its own (see `inherited` in [Data Model](#data-model) and
[List](#list-resort-room-beds) below). There is no partial merge between a room's own rows and its category's.

Resort room beds are always reached nested under their owning resort room; there is no top-level
`/api/v1/resort-room-beds` route. Every endpoint below also validates the
`{resort-id}`/`{resort-room-category-id}`/`{resort-room-id}` chain first — an unknown resort, an unknown
resort room category, an unknown resort room, or any of them belonging to the wrong parent, all return
`404 ENTITY_NOT_FOUND`. `{id}` on the single-row endpoints is additionally scoped to `{resort-room-id}` — a bed
`id` that exists but belongs to a different resort room behaves the same as an unknown `id`.

`bed_type_id` is **immutable after creation** — [Update Resort Room Bed](#update-resort-room-bed) only changes
`quantity`/`is_extra_bed_allowed`/`max_extra_beds`; to change the bed type, delete the row and create a new
one. A resort room may have at most one active row per bed type — attempting to add a second row for a bed
type it already has returns `409 CONFLICT`. All records support soft-delete — deleted records are hidden from
all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value still
shapes the response: it selects the locale-matched translation embedded on `bed_type.locale`, the same as `GET`
on [bed types](bed-types-api.md) directly.

---

## Endpoints

| Method | Path                                                                                                       | Description                  |
|--------|--------------------------------------------------------------------------------------------------------------|--------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`          | Create a resort room bed       |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`          | List a resort room's beds      |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`     | Get a resort room bed          |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`     | Update a resort room bed       |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`     | Delete a resort room bed       |

---

## Data Model

### ResortRoomBed

| Field                   | Type    | Required | Constraints                                                             | Description                                                                                       |
|--------------------------|---------|----------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| `id`                     | Long    | —        | read-only                                                                   | Auto-generated identifier                                                                              |
| `resort_room`            | Object  | —        | read-only; see [ResortRoom](resort-rooms-api.md); `null`/omitted when this row is inherited from the category | The resort room this bed row belongs to. Resolved from the URL path, never a request body field |
| `bed_type`               | Object  | —        | read-only; see [BedType](bed-types-api.md); resolved from `bed_type_id`     | The platform bed type this row represents. Immutable after creation                                    |
| `quantity`               | Integer | Yes      | default 1; >= 1                                                             | Number of beds of this type                                                                             |
| `is_extra_bed_allowed`   | Boolean | Yes      | default false                                                               | Whether an extra bed of this type can be added on request                                              |
| `max_extra_beds`         | Integer | Yes      | default 0; >= 0                                                             | Maximum extra beds of this type allowed                                                                 |
| `inherited`              | Boolean | —        | read-only; only meaningful on [List Resort Room Beds](#list-resort-room-beds) | `true` when the room has no bed rows of its own and this entry is actually the room's *category's* bed row instead; `false`/omitted for a room-owned row |

> **Note:** `bed_type_id` (used to resolve `bed_type`) is a write-only input, supplied only at creation — see
> [Create Resort Room Bed](#create-resort-room-bed) — and does not appear on this data model because the
> response always returns the resolved object instead. `bed_type` embeds only `id`, `code`, `sort_order`, and
> the Accept-Language-matched `locale` — a bed type's own nested collections are never embedded here.

---

## Create Resort Room Bed

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`

Adds a new bed row to an existing resort room. `bed_type_id` must reference an existing, active [Bed
Type](bed-types-api.md) — an unknown id returns `404 ENTITY_NOT_FOUND`. The combination of resort room and bed
type must be unique — adding a bed type it already has a row for returns `409 CONFLICT`.

### Path Parameters

| Parameter                 | Type | Description                            |
|-----------------------------|------|-------------------------------------------|
| `resort-id`                 | Long | ID of the owning resort                    |
| `resort-room-category-id`   | Long | ID of the owning resort room category      |
| `resort-room-id`            | Long | ID of the owning resort room               |

### Request Body

```json
{
  "bed_type_id": 4,
  "quantity": 2,
  "is_extra_bed_allowed": false,
  "max_extra_beds": 0
}
```

### Request Fields

| Field                   | Type    | Required | Validation                                                                        |
|--------------------------|---------|----------|----------------------------------------------------------------------------------------|
| `bed_type_id`            | Long    | Yes      | Not null; must reference an existing, active bed type; immutable after creation          |
| `quantity`                | Integer | Yes      | Not null; >= 1                                                                          |
| `is_extra_bed_allowed`    | Boolean | Yes      | Not null                                                                                |
| `max_extra_beds`          | Integer | Yes      | Not null; >= 0                                                                          |

### Response `201 Created`

```json
{
  "success": true,
  "id": 26
}
```

---

## Get Resort Room Bed

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`

Returns a single active resort room bed, scoped to its owning resort room — an `id` that exists but belongs to
a different resort room (or a resort room that belongs to a different resort room category) returns
`404 ENTITY_NOT_FOUND`, the same as an unknown `id`.

### Path Parameters

| Parameter                 | Type | Description                             |
|-----------------------------|------|--------------------------------------------|
| `resort-id`                 | Long | ID of the owning resort                     |
| `resort-room-category-id`   | Long | ID of the owning resort room category       |
| `resort-room-id`            | Long | ID of the owning resort room                |
| `id`                         | Long | ID of the resort room bed                   |

### Response `200 OK`

```json
{
  "data": {
    "id": 25,
    "resort_room": {
      "id": 20,
      "code": "R-101",
      "sort_order": 1,
      "floor_number": 1,
      "building": "Main Building"
    },
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
}
```

---

## List Resort Room Beds

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds`

Returns a paginated list of every active bed row belonging to the resort room, **if it has any of its own** —
optionally filtered to a single `bed_type_id`. **If the room has zero active bed rows of its own, this
instead returns its category's bed rows** (`inherited: true` on each, `resort_room` omitted since they aren't
really this room's own rows) — see the example below. The `bed_type_id` filter, pagination, and sort are
**not** applied to the inherited fallback list — the same simplification [List Resort Room
Prices](resort-room-prices-api.md#list-resort-room-prices) makes for its own category fallback bundle — every
active category bed row is returned on a single page.

> **Note:** `sortBy` accepts only `createdAt` — passing any other value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omitting `sortBy` entirely sorts by `id` (implicit).
> Only applies when the room has its own bed rows — see above.

### Path Parameters

| Parameter                 | Type | Description                             |
|-----------------------------|------|--------------------------------------------|
| `resort-id`                 | Long | ID of the owning resort                     |
| `resort-room-category-id`   | Long | ID of the owning resort room category       |
| `resort-room-id`            | Long | ID of the owning resort room                |

### Query Parameters

| Parameter      | Type   | Default         | Constraints      | Description                       |
|-----------------|--------|-------------------|--------------------|--------------------------------------|
| `bed_type_id`   | Long   | —                 | —                  | Filter to rows for this bed type      |
| `page`          | int    | `0`               | >= 0               | Zero-based page index                 |
| `size`          | int    | `10`              | 1 – 50             | Number of items per page              |
| `sortBy`        | String | `id` (implicit)   | `createdAt` only   | Field to sort by                      |
| `sortDir`       | String | `ASC`             | `ASC`, `DESC`      | Sort direction                        |

### Response `200 OK` — room has its own bed rows

```json
{
  "data": [
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
      "max_extra_beds": 1,
      "inherited": false
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

> **Note:** unlike [Get Resort Room Bed](#get-resort-room-bed), list rows omit `resort_room` entirely (`null`,
> dropped by `JsonInclude.NON_NULL`) — it's identical on every row and already known from the URL path.

### Response `200 OK` — room has no beds of its own, inherited from category

```json
{
  "data": [
    {
      "id": 6,
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
      "max_extra_beds": 1,
      "inherited": true
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 1,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt"
  ],
  "searchable_fields": []
}
```

`id` here (`6`) is the *category's* bed row id, not a room-level id — the room has no bed rows of its own in
this scenario. Pagination doesn't apply to the fallback bundle (see above) — `page_size` reflects the full
inherited list size instead of the requested `size`.

---

## Update Resort Room Bed

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`

Updates `quantity`, `is_extra_bed_allowed`, and `max_extra_beds` only. `bed_type_id` is set at creation and
cannot be changed — to change the bed type, delete the row and create a new one.

### Path Parameters

| Parameter                 | Type | Description                             |
|-----------------------------|------|--------------------------------------------|
| `resort-id`                 | Long | ID of the owning resort                     |
| `resort-room-category-id`   | Long | ID of the owning resort room category       |
| `resort-room-id`            | Long | ID of the owning resort room                |
| `id`                         | Long | ID of the resort room bed                   |

### Request Body

```json
{
  "quantity": 2,
  "is_extra_bed_allowed": true,
  "max_extra_beds": 1
}
```

### Request Fields

| Field                   | Type    | Required | Validation         |
|--------------------------|---------|----------|-----------------------|
| `quantity`                | Integer | Yes      | Not null; >= 1         |
| `is_extra_bed_allowed`    | Boolean | Yes      | Not null               |
| `max_extra_beds`          | Integer | Yes      | Not null; >= 0         |

### Response `200 OK`

```json
{
  "success": true,
  "id": 26
}
```

---

## Delete Resort Room Bed

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/beds/{id}`

Soft-deletes the resort room bed. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter                 | Type | Description                             |
|-----------------------------|------|--------------------------------------------|
| `resort-id`                 | Long | ID of the owning resort                     |
| `resort-room-category-id`   | Long | ID of the owning resort room category       |
| `resort-room-id`            | Long | ID of the owning resort room                |
| `id`                         | Long | ID of the resort room bed                   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 26
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
  "message": "ResortRoomBed not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                       |
|-------------|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields (`bed_type_id` null on create); an unsupported `sortBy` query value                                                                                                                                                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found; resort room not found for the given `resort-room-category-id`/`resort-room-id` pair (including a `resort-room-id` that belongs to a different resort room category); resort room bed not found for the given `resort-room-id`/`id` pair (including an `id` that belongs to a different resort room); the bed type referenced by `bed_type_id` not found |
| 409         | `CONFLICT`                 | The resort room already has an active bed row for the given `bed_type_id` (pre-checked at the application level)                                                                                                                                                                                                                                                                |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `(resort_room_id, bed_type_id)`, or a foreign key (`bed_type_id`, `resort_room_id`) somehow referencing a row that no longer exists — should not normally be reachable, since each is resolved and validated before the write                                                                                                        |
