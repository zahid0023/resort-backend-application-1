# Room Statuses API

Base URL: `/api/v1/room-statuses`

Room statuses represent the operational availability state of a physical resort room (e.g.
`AVAILABLE`, `MAINTENANCE`, `OUT_OF_ORDER`, `RENOVATION`), independent of the room's own soft-delete
lifecycle — a room can be temporarily pulled from booking availability (for upkeep, damage, or renovation)
without being deleted, then restored to `AVAILABLE` once the status changes back. Each status is identified
by a unique `code`. A status's display name and description are locale-specific and are managed through a
companion sub-resource — Room Status Locales — reached via
`/api/v1/room-statuses/{room-status-id}/locales`. The platform ships with four seeded statuses
(`AVAILABLE`, `MAINTENANCE`, `OUT_OF_ORDER`, `RENOVATION`), but this list is **not read-only** — additional
statuses can be created, and existing ones updated or deleted, through this API. All records support
soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Room Status)** and **`GET` (List/Search Room Statuses)** — the header's
  value selects exactly one locale translation for the status's `locale` field: an exact match if the
  status has one, otherwise `en`, otherwise `null`.
- **`GET /{room-status-id}/locales` (List Room Status Locales)** — the header must be
  present, but its value has no effect; this endpoint returns every translation (optionally filtered by
  `localeCode`), not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                   | Description                 |
|--------|--------------------------------------------------------|-----------------------------|
| POST   | `/api/v1/room-statuses`                                | Create a room status        |
| GET    | `/api/v1/room-statuses`                                | List / search room statuses |
| GET    | `/api/v1/room-statuses/{id}`                           | Get a room status           |
| PUT    | `/api/v1/room-statuses/{id}`                           | Update a room status        |
| DELETE | `/api/v1/room-statuses/{id}`                           | Delete a room status        |
| GET    | `/api/v1/room-statuses/{room-status-id}/locales`       | List a status's locales     |
| GET    | `/api/v1/room-statuses/{room-status-id}/locales/count` | Count a status's locales    |
| POST   | `/api/v1/room-statuses/{room-status-id}/locales`       | Create a status locale      |
| PUT    | `/api/v1/room-statuses/{room-status-id}/locales/{id}`  | Update a status locale      |
| DELETE | `/api/v1/room-statuses/{room-status-id}/locales/{id}`  | Delete a status locale      |

---

## Data Model

### RoomStatus

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                |
|--------------|---------|----------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                  |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code, e.g. `AVAILABLE`, `MAINTENANCE`, `OUT_OF_ORDER`, `RENOVATION`                                                               |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                              |
| `locale`     | Object  | —        | nullable; see RoomStatusLocale below                                  | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the status has no translations at all) |

### RoomStatusLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 100 chars                                    | Localized display name, e.g. `Under Maintenance`                               |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Room Status

`POST /api/v1/room-statuses`

Creates a new room status together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted statuses — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Room Status Locales sub-resource below.

### Request Body

```json
{
  "code": "DEEP_CLEAN",
  "sort_order": 5,
  "locale": {
    "name": "Deep Clean",
    "description": "The room is temporarily unavailable for a scheduled deep-cleaning pass.",
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
  "id": 5
}
```

---

## Get Room Status

`GET /api/v1/room-statuses/{id}`

Returns a single active room status by its ID. `locale` is the one translation matching the
request's `Accept-Language` header (falls back to `en`, then `null` if the status has no translations at
all). To fetch every translation a status has, use
[List Room Status Locales](#list-room-status-locales) below.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the room status |

### Response `200 OK`

```json
{
  "data": {
    "id": 2,
    "code": "MAINTENANCE",
    "sort_order": 2,
    "locale": {
      "id": 2,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Under Maintenance",
      "description": "The room is temporarily unavailable for routine upkeep or repairs.",
      "sort_order": 2
    }
  }
}
```

---

## List / Search Room Statuses

`GET /api/v1/room-statuses`

Returns a paginated, filterable list of active (non-deleted) room statuses. All filter parameters
are optional; omitting them returns all statuses. Each `LIKE`-type filter performs a case-insensitive
partial match. `Accept-Language` selects each status's `locale` field the same way as `GET /{id}` (exact
match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely. There is also no `name`-based sort or filter for this entity — `locale.name` is
> intentionally not exposed as a search/sort field (only `localeCode` filtering is available on the locale
> sub-resource's own list endpoint below).

### Query Parameters

> **Note:** Query parameters bind directly onto `RoomStatusFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming`
> (which produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute` /
> `@ParameterObject` query-string binding goes through Spring's plain `DataBinder` instead, which
> matches the exact property name.

| Parameter | Type   | Default         | Constraints                               | Description                                                           |
|-----------|--------|-----------------|-------------------------------------------|-----------------------------------------------------------------------|
| `code`    | String | —               | —                                         | Filter by internal code (partial, case-insensitive), e.g. `AVAILABLE` |
| `page`    | int    | `0`             | >= 0                                      | Zero-based page index                                                 |
| `size`    | int    | `10`            | 1 – 50                                    | Number of items per page                                              |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code` (`id` NOT selectable) | Field to sort by                                                      |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                             | Sort direction                                                        |

### Response `200 OK`

```json
{
  "data": [
    {
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
        "description": "The room is in normal condition and can be booked.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "MAINTENANCE",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Under Maintenance",
        "description": "The room is temporarily unavailable for routine upkeep or repairs.",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 4,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "code"
  ],
  "searchable_fields": [
    "code"
  ]
}
```

---

## Update Room Status

`PUT /api/v1/room-statuses/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Room Status Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the room status |

### Request Body

```json
{
  "sort_order": 3
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
  "id": 2
}
```

---

## Delete Room Status

`DELETE /api/v1/room-statuses/{id}`

Soft-deletes the room status. The record is not removed from the database but will no longer appear
in any response.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the room status |

### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Room Status Locales

Room Status Locale endpoints manage locale-specific name/description translations for a room
status. The `{room-status-id}` path parameter must reference an existing, active status.

---

### List Room Status Locales

`GET /api/v1/room-statuses/{room-status-id}/locales`

Returns a paginated list of every locale translation belonging to a room status — this is the only
way to see more than the single Accept-Language-matched translation returned by
`GET /room-statuses/{id}` and `GET /room-statuses`. Optionally filtered to locales whose
`code` contains a given substring.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `room-status-id` | Long | ID of the parent room status |

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
      "id": 2,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Under Maintenance",
      "description": "The room is temporarily unavailable for routine upkeep or repairs.",
      "sort_order": 2
    },
    {
      "id": 9,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "রক্ষণাবেক্ষণাধীন",
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

### Count Room Status Locales

`GET /api/v1/room-statuses/{room-status-id}/locales/count`

Returns how many active locale translations a room status currently has, plus the `code` of each
one. Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the status is still missing and can add a translation for via
[Create Room Status Locale](#create-room-status-locale) — e.g. if the platform has `en`,
`bn`, `es` and this endpoint returns `en` for the status, `bn` and `es` are still available; if it returns
all three, every platform locale already has a translation and `POST .../locales` for any of them will
fail with `409 CONFLICT`.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `room-status-id` | Long | ID of the parent room status |

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

### Create Room Status Locale

`POST /api/v1/room-statuses/{room-status-id}/locales`

Adds a new locale translation to an existing room status. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of status and locale
must be unique — adding a locale the status already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint
(`uq_room_status_locale` on `room_status_id` + `locale_id`) as a last-resort guard).

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `room-status-id` | Long | ID of the parent room status |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রক্ষণাবেক্ষণাধীন",
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
  "id": 9
}
```

---

### Update Room Status Locale

`PUT /api/v1/room-statuses/{room-status-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing room status locale translation. The
associated status and locale cannot be changed after creation.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `room-status-id` | Long | ID of the parent room status |
| `id`             | Long | ID of the room status locale |

#### Request Body

```json
{
  "name": "রক্ষণাবেক্ষণাধীন",
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
  "id": 9
}
```

---

### Delete Room Status Locale

`DELETE /api/v1/room-statuses/{room-status-id}/locales/{id}`

Soft-deletes a room status locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `room-status-id` | Long | ID of the parent room status |
| `id`             | Long | ID of the room status locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 9
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
  "message": "RoomStatus not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                               |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value               |
| 404         | `ENTITY_NOT_FOUND`         | Room status not found, room status locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                            |
| 409         | `CONFLICT`                 | `code` already in use by another active room status (`create`); or the status already has a translation for the given `locale_id` (`create` status locale, pre-checked at the application level)    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint (`uq_room_status_locale`) on `room_status_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level |
